import { Routes, Route, Navigate } from "react-router-dom";
import { unstable_HistoryRouter as Router } from "react-router-dom";
import { SignInPage } from "./pages/signInPage";
import { NewAccountPage } from "./pages/newAccountPage";
import history from "./components/history";
import { createTheme, Divider, ThemeProvider } from "@mui/material";
import { MainPage } from "./pages/mainPage";
import CreatePacketStepper from "./components/createPacket/createPacketStepper";
import { createPacketSteps } from "./data/createPacketSteps";
import axios from "axios";

const theme = createTheme({
  palette: {
    primary: {
      main: "#424242",
    },
    secondary: {
      main: "#ff8a65",
    },
  },
});


// if(decodedToken.exp < dateNow.getTime())
//     isExpired = true;


export function ProtectedWrapper({ children }: any) {
  //Grab the existing jwt out of local storage, if there isn't one default to redirecting to login page
  let localJWT = localStorage.getItem("jwt")
  
  //If localJWT doesn't exist redirect to login page
  if(localJWT === null){
    return <Navigate to="/" replace />;
  }

  axios
    .post("/api/user/auth", {jwt: localJWT})
      .then((authRes) => {
        const authData = JSON.stringify(authRes.data);
        const authJSON = JSON.parse(authData);
        const validFlag = authJSON.validJWT;

        if (validFlag) { // True - work as normal. False - Redirect to sign in. Check if there's a JWT in Local storage.
          return children;
        } else {
          return <Navigate to="/" replace />;
        }
      })
      .catch((e: any) => {
        return <Navigate to="/" replace />;
      });
  return <Navigate to="/" replace />;
}

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Router history={history}>
        <div className="min-vh-75 d-flex justify-content-center align-items-center">
          <Routes>
            <Route path="/" element={<SignInPage />} />
            <Route path="/newuser" element={<NewAccountPage />} />
            <Route
              path="/home"
              element={<ProtectedWrapper><MainPage pageContent={<p>Welcome home :)</p>} /></ProtectedWrapper>}
            />
            <Route
              path="/createpacket/:id"
              element={
                <MainPage
                  pageContent={
                    <CreatePacketStepper
                      steps={createPacketSteps}
                      stepsFinishedComponent={() => (
                        <div>
                          <Divider />
                          <p>You did it :)</p>
                        </div>
                      )}
                    />
                  }
                />
              }
            />
          </Routes>
        </div>
      </Router>
    </ThemeProvider>
  );
}

export default App;
