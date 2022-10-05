import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { ApprovalStep } from "../components/createPacket/approvalStep";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";
import userEvent from "@testing-library/user-event";

let serverResponse = 0;
const server: SetupServerApi = setupServer(
  rest.post("/api/highlightpdf", async (req, res, ctx) => {
    return res(ctx.status(serverResponse));
  })
);

const makeServerBeforeTest = (response: number) => {
  serverResponse = response;
  server.listen();
};

afterEach(() => server.resetHandlers());
afterAll(() => server.close);

test("File must be selected before clicking save", () => {
  let completed = false;
  render(
    <ApprovalStep
      completeStep={() => {
        completed = true;
      }}
    />
  );
  fireEvent.click(screen.getByText("Save"));
  expect(screen.getByText("You must select a PDF file")).toBeTruthy();
});

test("Selected file must be a PDF", () => {
  let completed = false;
  render(
    <ApprovalStep
      completeStep={() => {
        completed = true;
      }}
    />
  );
  let file = new File(["blob"], "coolImage.png", { type: "image/png" });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  expect(screen.getByText("File selected is not a PDF")).toBeTruthy();
});

test("400 response creates alert", async () => {
  makeServerBeforeTest(400);
  let completed = false;
  render(
    <ApprovalStep
      completeStep={() => {
        completed = true;
      }}
    />
  );

  let file = new File(["blob"], "coolDocument.pdf", {
    type: "application/pdf",
  });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  fireEvent.click(screen.getByText("Save"));
  await waitFor(() =>
    expect(screen.getByText("Failed to save PDF")).toBeTruthy()
  );
});

test("Step is completed with 200 response", async () => {
  makeServerBeforeTest(200);
  let completed = false;
  render(
    <ApprovalStep
      completeStep={() => {
        completed = true;
      }}
    />
  );

  let file = new File(["blob"], "coolDocument.pdf", {
    type: "application/pdf",
  });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  fireEvent.change(screen.getByLabelText("Highlight Words"), {
    target: { value: "broken" },
  });
  fireEvent.click(screen.getByText("Save"));
  await waitFor(() => expect(completed).toBe(true));
});
