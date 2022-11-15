import { Paper } from "@mui/material";
import React from "react";
import { useEffect } from "react";
import { NavBar, NavInfo } from "../components/navBar";
import history from "../components/history";
import { getHttpService } from "../data/httpService";

export interface MainPageProps {
  pageContent: React.ReactNode;
}

export function MainPage(props: MainPageProps) {
  useEffect(() => {
    const httpService = getHttpService();
    httpService.axios
      .post<any>("/api/user/auth")
      .then((authRes) => {
        const validFlag = authRes.data.validJWT;

        if (!validFlag) {
          history.push("/");
        }
      })
      .catch((e: any) => {
        // Refreshing the page as fast as you can shouldn't send you to login
        if (!e.message.includes("abort")) {
          history.push("/");
        }
      });
  }, []);

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
      href: "/logout",
    },
  ];

  return (
    <Paper style={styles.outerBox}>
      <NavBar content={navItems} basePath="f22-gneiss" barWidth="20vw" />
      <div style={styles.centerBox}>{props.pageContent}</div>
    </Paper>
  );
}
