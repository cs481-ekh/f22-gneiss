import { NewAccountForm } from "../components/newAccountForm";
import logo from "../images/logo.png";
import image from "../images/bsu.jpg";

export interface NewAccountPageProps {}

export function NewAccountPage(props: NewAccountPageProps) {
  const styles = {
    signIn: {
      display: "flex",
      flexDirection: "column",
      height: "100vh",
      alignItems: "center",
      justifyContent: "center",
      backgroundImage: `url(${image})`,
    } as const,
    logo: {
      width: "500px",
      margin: "20px",
    },
  };

  return (
    <div style={styles.signIn}>
      <img style={styles.logo} alt="Boise State University logo" src={logo} />
      <NewAccountForm />
    </div>
  );
}
