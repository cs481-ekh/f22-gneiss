import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/lib/node";
import { PacketsPage } from "../pages/packetsPage";

let serverResponse = 0; //setting up fake server to send requests to.
const server: SetupServerApi = setupServer(
  rest.post("/api/packet/:id", async (req, res, ctx) => {
    return res(ctx.status(serverResponse));
  })
);
const makeServerBeforeTest = (response: number) => {
  serverResponse = response;
  server.listen();
};

test("Clicking create button adds new packet", () => {
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  expect(screen.getByLabelText("New packet")).toBeTruthy();
});

test("Create button and search bar are disabled when naming new packet", () => {
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  expect(screen.getByText("Create").closest("button")).toBeDisabled();
  expect(
    screen.getByLabelText("Search packets").closest("input")
  ).toBeDisabled();
});

test("New packet must be at least 3 characters", () => {
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  fireEvent.change(screen.getByLabelText("New packet"), {
    target: { value: "12" },
  });
  fireEvent.blur(screen.getByLabelText("New packet"));
  expect(screen.getByText("Must be at least 3 characters")).toBeTruthy();
});

test("New packet must be no more than 20 characters", () => {
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  fireEvent.change(screen.getByLabelText("New packet"), {
    target: { value: "123456789012345678901" },
  });
  fireEvent.blur(screen.getByLabelText("New packet"));
  expect(screen.getByText("Must not exceed 20 characters")).toBeTruthy();
});

test("Clicking off input when packet name is valid creates new packet", async () => {
  makeServerBeforeTest(200);
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  fireEvent.change(screen.getByLabelText("New packet"), {
    target: { value: "Demo Packet" },
  });
  fireEvent.blur(screen.getByLabelText("New packet"));
  await waitFor(() =>
    expect(() => screen.getByText("Demo Packet")).toBeTruthy()
  );
});

test("Server error on packet creation deletes packet and shows error text", async () => {
  makeServerBeforeTest(400);
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  fireEvent.change(screen.getByLabelText("New packet"), {
    target: { value: "Demo Packet" },
  });
  fireEvent.blur(screen.getByLabelText("New packet"));
  await waitFor(() =>
    expect(() =>
      screen.getByText("Failed to create packet. Try again.")
    ).toBeTruthy()
  );
  await waitFor(() => expect(() => screen.getByText("Demo Packet")).toThrow());
});
