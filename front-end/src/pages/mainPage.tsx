import { Paper } from "@mui/material";
import React from "react";
import { NavBar, NavInfo } from "../components/navBar";

export interface MainPageProps {
  pageContent: React.ReactNode;
}

export function MainPage(props: MainPageProps) {
  const styles = {
    outerBox: {
      display: "flex",
    } as const,
    centerBox: {
      height: "100vh",
      width: "80vw",
    },
  };

  const navItems: NavInfo[] = [
    {
      label: "Packets",
      href: "/home",
    },
    {
      label: "Sign out",
      href: "/",
    },
  ];

  return (
    <Paper style={styles.outerBox}>
      <NavBar content={navItems} barWidth="20vw" />
      <div style={styles.centerBox}>{props.pageContent}</div>
    </Paper>
  );
}
