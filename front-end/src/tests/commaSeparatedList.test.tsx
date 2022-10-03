import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { useState } from "react";
import { CommaSeparatedList } from "../components/createPacket/commaSeparatedList";

function ListParent() {
  const [wordSet, setWordSet] = useState(new Set<string>());
  return (
    <div>
      <h1>{wordSet.size}</h1>
      <CommaSeparatedList wordSet={wordSet} setWordSet={setWordSet} />
    </div>
  );
}

test("Each item in input list has a chip associated with it", () => {
  render(<ListParent />);
  const list = "test1,test2,test3,test4,test5";
  fireEvent.change(screen.getByLabelText("Highlight Words"), {
    target: { value: list },
  });
  list.split(",").forEach((v) => {
    expect(screen.getByText(v)).toBeTruthy();
  });
});

test("Repeat items only appear in the set once", () => {
  render(<ListParent />);
  const list = "test1,test1,test1,test1,test1";
  fireEvent.change(screen.getByLabelText("Highlight Words"), {
    target: { value: list },
  });
  expect(screen.getByText("1")).toBeTruthy();
});

test("Empty items don't appear in the set", () => {
  render(<ListParent />);
  const list = ",,,test1,,test2,,,,,test3";
  fireEvent.change(screen.getByLabelText("Highlight Words"), {
    target: { value: list },
  });
  expect(screen.getByText("3")).toBeTruthy();
});

test("Removed items are taken out of input field", () => {
  render(<ListParent />);
  const list = "test1,test2,test3";
  fireEvent.change(screen.getByLabelText("Highlight Words"), {
    target: { value: list },
  });
  fireEvent.click(screen.getAllByTestId("CancelIcon")[1]);
  expect(screen.getByLabelText("Highlight Words")).toHaveValue("test1,test3");
});
