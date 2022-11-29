import {
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";

export type NavInfo = {
  label: string;
  href: string;
};

export interface INavBarProps {
  content: NavInfo[];
  basePath: string;
  barWidth: string;
}

export function NavBar(props: INavBarProps) {
  const styles = {
    drawer: {
      width: props.barWidth,
    },
  };

  /**
   * ${props.basePath} in href was tacking on an extra /f22-gneiss
   * or extra basePath String while creating packet.
   */
  const mapLinks = props.content.map((v) => (
    <ListItem key={v.label} disablePadding>
      <ListItemButton component="a" href={`/f22-gneiss${v.href}`}>
        <ListItemText primary={v.label} />
      </ListItemButton>
    </ListItem>
  ));

  return (
    <Drawer
      style={styles.drawer}
      sx={{
        "& .MuiDrawer-paper": {
          width: props.barWidth,
          boxSizing: "border-box",
        },
      }}
      variant="permanent"
      anchor="left"
    >
      <List>{mapLinks}</List>
    </Drawer>
  );
}
