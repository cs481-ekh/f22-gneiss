import { useTheme } from "@mui/material";
import React from "react";
import { useEffect } from "react";
import { NavBar, NavInfo } from "../components/navBar";
import axios from "axios";
import history from "../components/history";

export interface MainPageProps {
  pageContent: React.ReactNode;
}

export function MainPage(props: MainPageProps) {
  useEffect(() => {
    let localJWT = localStorage.getItem("jwt");

    //If localJWT doesn't exist redirect to login page
    if (localJWT === null) {
      history.push("/");
    }
    axios
      .post("/api/user/auth", { jwt: localJWT })
      .then((authRes) => {
        const authData = JSON.stringify(authRes.data);
        const authJSON = JSON.parse(authData);
        const validFlag = authJSON.validJWT;

        if (validFlag) {
          // True - work as normal. False - Redirect to sign in. Check if there's a JWT in Local storage.
          history.push("home");
        } else {
          history.push("/");
        }
      })
      .catch((e: any) => {
        history.push("/");
      });
  }, []);
  const theme = useTheme();
  const styles = {
    outerBox: {
      display: "flex",
      backgroundColor: theme.palette.primary.light,
    } as const,
    centerBox: {
      display: "flex",
      flexDirection: "column",
      height: "100vh",
      width: "80vw",
      alignItems: "center",
      justifyContent: "center",
    } as const,
  };

  const navItems: NavInfo[] = [
    {
      label: "Home",
      href: "/home",
    },
    {
      label: "Create Packet",
      href: "/createpacket/temporary-hardcoded-id",
    },
    {
      label: "Sign out",
      href: "/",
    },
  ];

  return (
    <div style={styles.outerBox}>
      <NavBar content={navItems} barWidth="20vw" />
      <div style={styles.centerBox}>{props.pageContent}</div>
    </div>
  );
}
