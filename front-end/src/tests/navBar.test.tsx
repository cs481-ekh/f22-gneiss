import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { NavBar, NavInfo } from "../components/navBar";

const navItems: NavInfo[] = [
  {
    label: "Home",
    href: "/home",
  },
  {
    label: "Create Packet",
    href: "/createpacket",
  },
  {
    label: "Sign out",
    href: "/",
  },
];

test.each(navItems)("Check if Nav Bar have %s link.", (item) => {
  render(<NavBar content={navItems} basePath="" barWidth="20vw" />);
  expect(screen.getByText(item.label)).toBeTruthy();
});
