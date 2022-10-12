import { Routes, Route } from "react-router-dom";
import { unstable_HistoryRouter as Router } from "react-router-dom";
import { SignInPage } from "./pages/signInPage";
import { NewAccountPage } from "./pages/newAccountPage";
import history from "./components/history";
import { createTheme, Divider, ThemeProvider } from "@mui/material";
import { MainPage } from "./pages/mainPage";
import CreatePacketStepper from "./components/createPacket/createPacketStepper";
import { createPacketSteps } from "./data/createPacketSteps";

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

function App() {
  //App.use('/', viewRouter);
  //App.use('/auth/', authRouter);
  return (
    <ThemeProvider theme={theme}>
      <Router history={history}>
        <div className="min-vh-75 d-flex justify-content-center align-items-center">
          <Routes>
            <Route path="/" element={<SignInPage />} />
            <Route path="/newuser" element={<NewAccountPage />} />
            <Route
              path="/home"
              element={<MainPage pageContent={<p>Welcome home :)</p>} />}
            />
            <Route
              path="/createpacket"
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
