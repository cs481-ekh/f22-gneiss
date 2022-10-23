import { render, fireEvent, screen } from "@testing-library/react";
import { PacketsPage } from "../pages/packetsPage";

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

test("Clicking off input when packet name is valid creates new packet", () => {
  render(<PacketsPage />);
  fireEvent.click(screen.getByText("Create"));
  fireEvent.change(screen.getByLabelText("New packet"), {
    target: { value: "Demo Packet" },
  });
  fireEvent.blur(screen.getByLabelText("New packet"));
  expect(screen.getByText("Demo Packet")).toBeTruthy();
});
