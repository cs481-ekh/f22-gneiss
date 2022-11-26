import { Button, Divider, Paper, TextField } from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import { getHttpService } from "../data/httpService";
import jwt_decode from "jwt-decode";

export interface AdminPageProps {}

export function AdminPage(props: AdminPageProps) {
  const styles = {
    page: {
      padding: "32px",
    },
    header: {
      display: "flex",
      flexDirection: "column",
      gap: "16px",
      padding: "32px",
    } as const,
    controls: {
      display: "flex",
      gap: "8px",
    },
    packetList: {
      display: "flex",
      flexDirection: "column-reverse",
      gap: "8px",
    } as const,
    packet: {
      padding: "16px",
      display: "flex",
      justifyContent: "space-between",
    },
  };

  const [users, setUsers] = useState<
    { email: string; role: string; banned: boolean }[]
  >([]);
  const [search, setSearch] = useState("");
  const httpService = getHttpService();
  const loggedInUser = (jwt_decode(httpService.getAuth()) as any).sub;

  useEffect(() => {
    const httpService = getHttpService();
    httpService.axios.get<any>("/api/user/").then((res) => {
      setUsers(res.data.users);
    });
  }, []);

  const handleSearchChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const toggleBan = (index: number) => {
    let nu = users.slice();
    nu[index].banned = !nu[index].banned;
    httpService.axios.post("/api/user/ban", {
      email: nu[index].email,
      banned: nu[index].banned,
    });
    setUsers(nu);
  };

  const toggleRole = (index: number) => {
    let nu = users.slice();
    if (nu[index].role === "admin") {
      nu[index].role = "user";
      httpService.axios.post("/api/user/demote", { email: nu[index].email });
    } else {
      nu[index].role = "admin";
      httpService.axios.post("/api/user/promote", { email: nu[index].email });
    }
    setUsers(nu);
  };

  const listUsers = users
    .filter((user) => user.email.toLowerCase().includes(search.toLowerCase()))
    .map((user, i) => (
      <Paper elevation={3} style={styles.packet} key={user.email}>
        <div style={styles.controls}>
          <p>{user.email}</p>
          <p>&#8212;</p>
          <p>{user.role}</p>
        </div>
        {loggedInUser !== user.email && (
          <div style={styles.controls}>
            <Button variant="contained" onClick={() => toggleRole(i)}>
              {user.role === "user" ? "promote" : "demote"}
            </Button>
            <Button variant="contained" onClick={() => toggleBan(i)}>
              {user.banned ? "Unban" : "Ban"}
            </Button>
          </div>
        )}
      </Paper>
    ));

  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <h1>Admin</h1>
        <Divider />
        <h2>Users</h2>
        <TextField
          variant="filled"
          label="Search users"
          value={search}
          onChange={handleSearchChange}
        />
      </header>
      <Paper style={styles.packetList}>{listUsers}</Paper>
    </div>
  );
}
