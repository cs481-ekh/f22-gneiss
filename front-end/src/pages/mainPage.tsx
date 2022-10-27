import { useTheme } from "@mui/material";
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
    const httpService = getHttpService()
    httpService.axios
      .post<any>("/api/user/auth")
      .then((authRes) => {
        const validFlag = authRes.data.validJWT;

        if (validFlag) {
          // True - work as normal. False - Redirect to sign in.
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
