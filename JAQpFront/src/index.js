import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { createBrowserHistory } from "history";
import ReactDOM from "react-dom/client";
import "./index.css";
import Auth from "./pages/Auth";
import Reg from "./pages/Reg";
import Main from "./pages/Main";
import QuizMain from "./pages/QuizMain";
import QuizAnswers from "./pages/QuizAnswers";
import Settings from "./pages/Settings";
import Constructor from "./pages/Constructor";
import reportWebVitals from "./reportWebVitals";
import "./fonts/FuturaPT-Medium.ttf";
import QuizResult from "./pages/QuizResult";
import User from "./pages/User";
import FindQuiz from "./pages/FindQuiz";
import { transitions, positions, Provider as AlertProvider } from "react-alert";
import AlertTemplate from 'react-alert-template-basic'

export const history = createBrowserHistory();
const options = {
  position: positions.TOP_RIGHT,
  timeout: 5000,
  offset: "30px",
  transition: transitions.SCALE,
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <AlertProvider template={AlertTemplate} {...options}>
    <BrowserRouter history={history}>
      <Routes>
        <Route exact path="/login" element={<Auth />} />
        <Route exact path="/registration" element={<Reg />} />
        <Route exact path="/" element={<Main />} />
        <Route exact path="/:page/:text" element={<FindQuiz />} />
        <Route exact path="/constructor" element={<Constructor />} />
        <Route exact path="/user/settings" element={<Settings />} />
        <Route exact path="/quiz/:id/result" element={<QuizResult />} />
        <Route
          exact
          path="/quiz/:id/question/:idquestion"
          element={<QuizAnswers />}
        />
        <Route exact path="/quiz/:id" element={<QuizMain />} />
        <Route exact path="/user/:id" element={<User />} />
      </Routes>
    </BrowserRouter>
  </AlertProvider>
);

reportWebVitals();
