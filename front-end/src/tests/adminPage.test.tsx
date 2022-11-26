import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/lib/node";
import { AdminPage } from "../pages/adminPage";

const userList = [
  {
    email: "adminGuy",
    role: "admin",
    banned: false,
  },
  {
    email: "userGuy",
    role: "user",
    banned: false,
  },
  {
    email: "bannedGuy",
    role: "user",
    banned: true,
  },
];

const server = setupServer(
  rest.get("/api/user/", (req, res, ctx) => {
    return res(
      ctx.json({
        users: userList,
      })
    );
  }),
  rest.post("/api/user/promote", (req, res, ctx) => {
    return res(ctx.json({}));
  }),
  rest.post("/api/user/demote", (req, res, ctx) => {
    return res(ctx.json({}));
  }),
  rest.post("/api/user/ban", (req, res, ctx) => {
    return res(ctx.json({}));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test.each(userList)("Each user shows up in list", async (user) => {
  render(<AdminPage />);
  await waitFor(() => expect(screen.getByText(user.email)).toBeTruthy());
});
