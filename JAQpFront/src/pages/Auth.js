import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAlert } from "react-alert";

import { LoginUser } from "../http/userApi";

import Menu from "../components/Menu";

import maleIcon from "../icons/male.svg";
import passwordIcon from "../icons/password.svg";
import seeIcon from "../icons/see.svg";
import showIcon from "../icons/show.svg";

import "../css/auth.css";
import "../css/font.css";

export const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else delete axios.defaults.headers.common["Authorization"];
};

function Auth() {
  const alert = useAlert();
  /* navigate */
  const navigate = useNavigate();
  const gotoRegPage = () => navigate("/registration");
  const gotoMainPage = () => navigate("/");

  /* setters */
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [show, setShow] = useState(false);

  /* visual */
  const pass = useRef();

  const showPassword = () => {
    setShow(!show);
    pass.current.type = show ? "password" : "text";
  };

  /* form */
  const handleSubmit = (e) => {
    e.preventDefault();
    LoginUser(login, password).then((result) => {
      if (result) {
        alert.show(`Успешный вход`, { type: "success" });
        localStorage.setItem("token", result.data.jwtToken);
        localStorage.setItem("idUser", result.data.id);
        localStorage.setItem("userName", result.data.username);
        setAuthToken(result.data.jwtToken);
        gotoMainPage();
      } else alert.show(`Ошибка авторизации`, { type: "error" });
    });
  };

  return (
    <div
      className="auth_window"
      style={{ backgroundImage: "url(img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="auth_workspace">
        <div className="auth_whitecard">
          <div>
            <p className="h1">JAQp</p>
            <p className="h2">Авторизация</p>
          </div>
          <div>
            <div className="auth_evenly_distributed_field">
              <img className="auth_icon" src={maleIcon} alt=""></img>
              <input
                className="auth_input"
                placeholder="Логин"
                name="login"
                id="login"
                value={login}
                required
                onChange={(e) => setLogin(e.target.value)}
              ></input>
            </div>
            <div className="auth_evenly_distributed_field">
              <img className="auth_icon" src={passwordIcon} alt=""></img>
              <div className="auth_block_for_password_input">
                <input
                  className="auth_password_input"
                  ref={pass}
                  type={showPassword ? "text" : "password"}
                  placeholder="Пароль"
                  name="password"
                  id="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                ></input>
                {show ? (
                  <img
                    alt=""
                    className="auth_password_input_icon"
                    onClick={showPassword}
                    style={{ position: "absolute", right: "0", top: "0" }}
                    src={seeIcon}
                  ></img>
                ) : (
                  <img
                    alt=""
                    className="auth_password_input_icon"
                    onClick={showPassword}
                    style={{ position: "absolute", right: "0", top: "0" }}
                    src={showIcon}
                  ></img>
                )}
              </div>
            </div>
            <div className="auth_evenly_distributed_field">
              <button className="auth_textbutton" onClick={gotoRegPage}>
                Регистрация
              </button>
              <button className="auth_button" onClick={handleSubmit}>
                Войти
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Auth;
