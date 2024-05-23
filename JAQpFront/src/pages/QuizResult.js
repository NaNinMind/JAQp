import React from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";

import Menu from "../components/Menu";

import "../css/quizres.css";
import "../css/font.css";

function QuizResult() {
  /* setters */
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams();
  const { score, totalQuestions } = location.state || {};
  const percentage = (score / totalQuestions) * 100;

  const performanceMessages = {
    excellent:
      "Восхитительно! Вы настоящий эксперт в этой области. Поздравляем с таким впечатляющим результатом!",
    good: "Отличная работа! Вы показали хорошие знания. Еще чуть-чуть, и вы будете на пике!",
    average:
      "Неплохо! Вы ответили более чем на половину вопросов правильно. Еще немного усилий, и вы достигнете вершины!",
    belowAverage:
      "Вы на правильном пути, но есть еще куда стремиться. Возможно, немного повторения поможет улучшить результат.",
    poor: "Похоже, что сегодня не ваш день(",
  };

  function getPerformanceMessage(percentage) {
    switch (true) {
      case percentage >= 80:
        return performanceMessages.excellent;
      case percentage >= 60:
        return performanceMessages.good;
      case percentage >= 40:
        return performanceMessages.average;
      case percentage >= 20:
        return performanceMessages.belowAverage;
      default:
        return performanceMessages.poor;
    }
  }

  const performanceMessage = getPerformanceMessage(percentage);

  return (
    <div
      className="quizres_window"
      style={{ backgroundImage: "url(/../img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="quizres_workspace">
        <div className="results_container">
          <h1>Ваш результат</h1>
          <div>
            <p>
              Вы ответили правильно на {score} из {totalQuestions} вопросов.
            </p>
            <p>Это составляет {percentage.toFixed(2)}% правильных ответов.</p>
          </div>
          <p>{performanceMessage}</p>
          <div style={{ whiteSpace: "nowrap" }}>
            <button
              className="results_textbutton"
              onClick={() => {
                navigate(`/quiz/${id}`);
              }}
            >
              Заново
            </button>
            <button
              className="results_button"
              onClick={() => {
                navigate("/");
              }}
            >
              К квизам
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default QuizResult;
