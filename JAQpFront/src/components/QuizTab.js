import React, { useState, useEffect } from "react";
import { useAlert } from "react-alert";

import { CreateNewQuiz } from "../http/quizApi";
import { GetQuizById } from "../http/quizApi";
import { address } from "../http/apiIndex";

import "../css/font.css";
import "../css/quiztab.css";

import emptyQuizIcon from "../icons/emptyQuiz.svg";

function QuizTab({ arrtest, onSelectId, onAddTest }) {
  /* setterts */
  const [selectId, setSelectId] = useState(null);
  const alert = useAlert();

  useEffect(() => {}, [selectId]);

  /* func */
  const handleSelectId = (id) => {
    setSelectId(id);
    GetQuizById(id)
      .then((res) => {
        onSelectId(res.data);
      })
      .catch((error) => {
        alert.show(`Ошибка получения данных квиза`, { type: "error" });
      });
  };

  const handleAddTest = () => {
    alert.show(`Создаю квиз...`);
    CreateNewQuiz(localStorage.getItem("token"))
      .then((res) => {
        if (res.status === 200) {
          alert.show(`Квиз успешно создан`, { type: "success" });
          onAddTest(res.data);
        } else alert.show(`Ошибка создания квиза`, { type: "error" });
      })
      .catch((error) => {
        alert.show(`Ошибка создания квиза`, { type: "error" });
      });
  };

  const listItems = arrtest.map((testname, index) => (
    <div className="card" key={index}>
      <img
        src={testname.image == null ? emptyQuizIcon : address + testname.image}
        alt=""
        className="card__img"
      />
      <span className="card__footer">
        <div class="background"></div>
        <div className="quizname">{testname.name}</div>
        <p class="information">{testname.description}</p>
        <div class="control">
          <button class="btn" onClick={() => handleSelectId(testname.id)}>
            Редактировать
          </button>
        </div>
      </span>
    </div>
  ));

  return (
    <div className="quiz_space">
      <div className="card" style={{ background: "#E3E7F2" }}>
        <span
          alt=""
          className="card__img"
          style={{
            objectFit: "contain",
            padding: "14px",
            width: "90%",
            height: "90%",
          }}
        />
        <span
          className="card__footer"
          style={{ background: "none", backdropFilter: "none" }}
        >
          <div
            class="control"
            style={{ marginLeft: "-100%", alignItems: "center" }}
          >
            <button class="addButton" onClick={handleAddTest}>
              + Новый квиз
            </button>
          </div>
        </span>
      </div>
      {listItems}
    </div>
  );
}

export default QuizTab;
