import { SignInForm } from "../components/signInForm";
import logo from "../images/logo.png";
import image from "../images/bsu.jpg";

export interface SignInPageProps {}

export function SignInPage(props: SignInPageProps) {
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
      <SignInForm />
    </div>
  );
}
