import http from "k6/http";
import { check, sleep } from "k6";
import { Trend, Rate } from "k6/metrics";

// ─── Image loaded at init stage (k6 requirement: open() must be top-level) ────
const avatarBytes = open("./avatar.png", "b");

// ─── Custom Metrics ────────────────────────────────────────────────────────────
const loginDuration = new Trend("login_duration");
const listUsersDuration = new Trend("list_users_duration");
const getUserDuration = new Trend("get_user_duration");
const errorRate = new Rate("error_rate");

// ─── Test Configuration ────────────────────────────────────────────────────────
export const options = {
  stages: [
    { duration: "15s", target: 10 }, // ramp up to 10 virtual users
    { duration: "30s", target: 10 }, // hold 10 virtual users
    { duration: "15s", target: 0 },  // ramp down to 0
  ],
  thresholds: {
    http_req_duration: ["p(95)<500"],  // 95% of requests must finish within 500ms
    error_rate: ["rate<0.01"],         // error rate must be below 1%
  },
};

// ─── Constants ─────────────────────────────────────────────────────────────────
const BASE_URL = "http://localhost:8080/java-profile";

const TEST_USER = {
  email: "loadtest@example.com",
  password: "Test@12345",
  name: "Load Test User",
  phone: "11999999999",
};

// ─── Setup: runs once before the test ─────────────────────────────────────────
export function setup() {
  // Create user — POST /users requires multipart/form-data with an image field
  const createRes = http.post(`${BASE_URL}/users`, {
    name: TEST_USER.name,
    phone: TEST_USER.phone,
    email: TEST_USER.email,
    password: TEST_USER.password,
    image: http.file(avatarBytes, "avatar.png", "image/png"),
  });

  // 201 = created, 409 = already exists — both are fine to proceed
  if (createRes.status !== 201 && createRes.status !== 409) {
    console.error(`User creation failed [${createRes.status}]: ${createRes.body}`);
  }

  // Login and return token + userId to be shared across all VUs
  const loginRes = http.post(
    `${BASE_URL}/auth/login`,
    JSON.stringify({ email: TEST_USER.email, password: TEST_USER.password }),
    { headers: { "Content-Type": "application/json" } }
  );

  if (loginRes.status !== 200) {
    throw new Error(`Login failed [${loginRes.status}]: ${loginRes.body}`);
  }

  const token = loginRes.json("token");
  const userId = createRes.status === 201 ? createRes.json("id") : null;

  return { token, userId };
}

// ─── Default Function: runs for every virtual user ─────────────────────────────
export default function (data) {
  // --- Scenario 1: Login (get a fresh token for this iteration) ---
  const loginRes = http.post(
    `${BASE_URL}/auth/login`,
    JSON.stringify({ email: TEST_USER.email, password: TEST_USER.password }),
    { headers: { "Content-Type": "application/json" } }
  );

  loginDuration.add(loginRes.timings.duration);
  errorRate.add(loginRes.status !== 200);

  check(loginRes, {
    "login: status is 200": (r) => r.status === 200,
    "login: has token": (r) => r.json("token") !== undefined,
  });

  if (loginRes.status !== 200) {
    console.error(`[VU ${__VU}] login failed — status=${loginRes.status} body=${loginRes.body}`);
    return;
  }

  // Use the fresh token from this iteration's login
  const token = loginRes.json("token");
  const authHeaders = {
    headers: { Authorization: `Bearer ${token}` },
  };

  sleep(0.5);

  // --- Scenario 2: List users (paginated) ---
  const listRes = http.get(`${BASE_URL}/users?page=0&size=20`, authHeaders);

  listUsersDuration.add(listRes.timings.duration);
  errorRate.add(listRes.status !== 200);

  if (listRes.status !== 200) {
    console.error(`[VU ${__VU}] list users failed — status=${listRes.status} body=${listRes.body}`);
  }

  check(listRes, {
    "list users: status is 200": (r) => r.status === 200,
  });

  sleep(0.5);

  // --- Scenario 3: Get user by ID (only if we have it from setup) ---
  if (data.userId) {
    const getUserRes = http.get(
      `${BASE_URL}/users/${data.userId}`,
      authHeaders
    );

    getUserDuration.add(getUserRes.timings.duration);
    errorRate.add(getUserRes.status !== 200);

    if (getUserRes.status !== 200) {
      console.error(`[VU ${__VU}] get user failed — status=${getUserRes.status} body=${getUserRes.body}`);
    }

    check(getUserRes, {
      "get user: status is 200": (r) => r.status === 200,
      "get user: has email": (r) => r.json("email") !== undefined,
    });
  }

  sleep(1);
}
