import { Routes, Route } from "react-router-dom";
import { unstable_HistoryRouter as Router } from "react-router-dom";
import { LandingPage } from "./pages/landingPage";
import { SignInPage } from "./pages/signInPage";
import { NewAccountPage } from "./pages/newAccountPage";
import history from "./components/history";

function App() {
  return (
    <Router history={history}>
      <div className="min-vh-75 d-flex justify-content-center align-items-center">
        <Routes>
          <Route path="/" element={<SignInPage />} />
          <Route path="/home" element={<LandingPage />} />
          <Route path="/newuser" element={<NewAccountPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
