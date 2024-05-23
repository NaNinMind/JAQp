import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

import Menu from "../components/Menu";
import pencilIcon from "../icons/pencil.svg";
import settingsIcon from "../icons/settings.svg";
import logoutIcon from "../icons/logout.svg";
import { GetUserGeneral } from "../http/userApi";
import { Logout } from "../http/userApi";
import { GetOwned } from "../http/quizApi";
import { GetUserResults } from "../http/resultApi";
import { address } from "../http/apiIndex";
import emptyQuizIcon from "../icons/emptyQuiz.svg";
import { useAlert } from "react-alert";

import "../css/user.css";
import "../css/font.css";

function User() {
  /* setterts */
  const [startDate, setStartDate] = useState();
  const alert = useAlert();

  const [preffirstName, setprefFirstName] = useState("");
  const [prefsecondName, setprefSecondName] = useState("");
  const [preflastName, setprefLastName] = useState("");
  const { id } = useParams();

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("completed");
  const [prefQuizList, setprefQuizList] = useState([]);
  const [prefQuizListCompleted, setprefQuizListCompleted] = useState([]);
  useEffect(() => {
    GetOwned(id)
      .then((res) => {
        setprefQuizList(res.data.quizDataList);
      })
      .catch((error) => {
        alert.show("Ошибка полученния данных пользователя", { type: "error" });
      });
  }, [id]);
  useEffect(() => {
    GetUserResults(id)
      .then((res) => {
        setprefQuizListCompleted(res.data.results);
      })
      .catch((error) => {
        alert.show("Ошибка полученния данных пользователя", { type: "error" });
      });
  }, [id]);

  useEffect(() => {
    GetUserGeneral(id).then((res) => {
      if (res.status === 200) {
        setprefFirstName(res.data.firstName);
        setprefSecondName(res.data.secondName);
        setprefLastName(res.data.lastName);

        setStartDate(
          res.data.birthDate
            ? new Date(res.data.birthDate).toLocaleString("ru-RU", {
                timeZone: "Europe/Moscow",
                year: "numeric",
                month: "long",
                day: "numeric",
              })
            : null
        );
      } else
        alert.show("Ошибка полученния данных пользователя", { type: "error" });
    });
  }, [id]);

  const listItems = prefQuizList.map((testname, index) => (
    <div
      className="card"
      key={index}
      onClick={() => navigate(`/quiz/${testname.id}`)}
    >
      <img
        src={testname.image == null ? emptyQuizIcon : address + testname.image}
        alt=""
        className="card__img"
      />
      <span className="card__footer">
        <div class="background"></div>
        <div className="quizname">{testname.name}</div>
      </span>
    </div>
  ));

  const listItemsCompleted =
    prefQuizListCompleted &&
    prefQuizListCompleted.map((testname, index) => (
      <div
        className="card"
        key={index}
        onClick={() => navigate(`/quiz/${testname.id}`)}
      >
        <img
          src={
            testname.quizData.image === null || testname.quizData.image === ""
              ? emptyQuizIcon
              : address + testname.quizData.image
          }
          alt=""
          className="card__img"
        />
        <span className="card__footer">
          <div className="quizname">{testname.quizData.name}</div>
          <div className="quizname">
            {"Результат: " + testname.result * 100 + "%"}
          </div>
        </span>
      </div>
    ));

  return (
    <div
      className="user_window"
      style={{ backgroundImage: "url(../img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="user_workspace">
        <div>
          <div className="user_whitecard_with_space" style={{ width: "680px" }}>
            <div className="user_whitecard_with_space">
              <p className="h2">Пользователь</p>
            </div>
            <div className="user_evenly_distributed_field">
              <div>
                {preffirstName && (
                  <div style={{ display: "flex" }}>
                    <p
                      style={{
                        minWidth: "5rem",
                        paddingTop: "24px",
                        paddingRight: "14px",
                        paddingLeft: "14px",
                        alignSelf: "center",
                        fontFamily: "Futura PT",
                      }}
                    >
                      Имя
                    </p>
                    <div class="omrs-input-group">
                      <label class="omrs-input-filled">
                        <p required maxLength={255} />
                        <span
                          class="omrs-input-label"
                          style={{ top: "2rem", cursor: "text" }}
                        >
                          {preffirstName}
                        </span>
                      </label>
                    </div>
                  </div>
                )}
                {prefsecondName && (
                  <div style={{ display: "flex" }}>
                    <p
                      style={{
                        minWidth: "5rem",
                        paddingTop: "24px",
                        paddingRight: "14px",
                        paddingLeft: "14px",
                        fontFamily: "Futura PT",
                      }}
                    >
                      Фамилия
                    </p>
                    <div class="omrs-input-group">
                      <label class="omrs-input-filled">
                        <p required maxLength={255} />
                        <span
                          class="omrs-input-label"
                          style={{ top: "2rem", cursor: "text" }}
                        >
                          {prefsecondName}
                        </span>
                      </label>
                    </div>
                  </div>
                )}
                {preflastName && (
                  <div style={{ display: "flex" }}>
                    <p
                      style={{
                        minWidth: "5rem",
                        paddingTop: "24px",
                        paddingRight: "14px",
                        paddingLeft: "14px",
                        fontFamily: "Futura PT",
                      }}
                    >
                      Отчество
                    </p>
                    <div class="omrs-input-group">
                      <label class="omrs-input-filled">
                        <p required maxLength={255} />
                        <span
                          class="omrs-input-label"
                          style={{ top: "2rem", cursor: "text" }}
                        >
                          {preflastName}
                        </span>
                      </label>
                    </div>
                  </div>
                )}
                {startDate && (
                  <div style={{ display: "flex" }}>
                    <p
                      style={{
                        padding: "14px",
                        minWidth: "5rem",
                        alignSelf: "center",
                        fontFamily: "Futura PT",
                      }}
                    >
                      День рождения
                    </p>
                    <div class="omrs-input-group">
                      <label class="omrs-input-filled">
                        <p required maxLength={255} />
                        <span
                          class="omrs-input-label"
                          style={{ top: "2rem", cursor: "text" }}
                        >
                          {startDate}
                        </span>
                      </label>
                    </div>
                  </div>
                )}
                {id === localStorage.getItem("idUser") &&
                  !(
                    startDate &&
                    preffirstName &&
                    preflastName &&
                    prefsecondName
                  ) && (
                    <button
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        marginTop: "14px",
                      }}
                      className="results_textbutton"
                      onClick={() => {
                        navigate(`/user/settings`);
                      }}
                    >
                      <img
                        style={{ width: "24px", marginRight: "14px" }}
                        src={pencilIcon}
                        alt=""
                      ></img>
                      Заполнить данные
                    </button>
                  )}
              </div>
              {id === localStorage.getItem("idUser") && (
                <div
                  style={{
                    marginLeft: "3rem",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "space-between",
                    maxHeight: "128px",
                    marginTop: "0.5rem",
                  }}
                >
                  <button
                    style={{
                      display: "flex",
                      color: "red",
                      alignItems: "center",
                    }}
                    className="results_textbutton"
                    onClick={() => {
                      Logout(localStorage.getItem("token")).then((res) => {
                        if (res.status === 200) {
                          localStorage.clear();
                          navigate("/login");
                        }
                      });
                    }}
                  >
                    <svg
                      xmlnsXlink="http://www.w3.org/1999/xlink"
                      style={{
                        width: "16px",
                        height: "16px",
                        marginRight: "14px",
                        fill: "red",
                      }}
                    >
                      <use xlinkHref={logoutIcon + "#logoutIcon"} />
                    </svg>
                    Выйти
                  </button>
                  <button
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                    className="results_textbutton"
                    onClick={() => {
                      navigate(`/user/settings`);
                    }}
                  >
                    <img
                      style={{ width: "24px", marginRight: "14px" }}
                      src={settingsIcon}
                      alt=""
                    ></img>
                    Настройки
                  </button>
                </div>
              )}
            </div>
            <div>
              <div className="tab_buttons">
                <button
                  onClick={() => setActiveTab("completed")}
                  className={activeTab === "completed" ? "active" : ""}
                >
                  Пройденные квизы
                </button>
                <button
                  onClick={() => setActiveTab("administered")}
                  className={activeTab === "administered" ? "active" : ""}
                >
                  Администрируемые квизы
                </button>
              </div>

              {activeTab === "completed" && (
                <div className="tab_content">{listItemsCompleted}</div>
              )}
              {activeTab === "administered" && (
                <div className="tab_content">{listItems}</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default User;
