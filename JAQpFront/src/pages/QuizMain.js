import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { address } from "../http/apiIndex";
import { useAlert } from 'react-alert'

import { GetQuizById } from "../http/quizApi";
import { GetQuestions } from "../http/quizApi";

import Menu from "../components/Menu";
import "../css/quizmain.css";
import "../css/font.css";
import emptyQuizIcon from "../icons/emptyQuiz.svg";

function Quiz() {
  const alert = useAlert();
  /* navigate */
  const goToQuiz = () => {
    const questionIds =
      JSON.parse(sessionStorage.getItem(`questions_${id}`)) || [];

    navigate(`/quiz/${id}/question/${questionIds.questions[0]}`);
  };
  /* setters */
  const { id } = useParams();
  const [quizData, setQuizData] = useState(null);
  const [questions, setQuestions] = useState(null);
  const navigate = useNavigate();
  
  useEffect(() => {
    async function fetchQuizData() {
      try {
        const response = await GetQuizById(id);
        setQuizData(response.data);
        sessionStorage.clear();
        const cachedQuestions = sessionStorage.getItem(`questions_${id}`);
        if (cachedQuestions) {
          setQuestions(JSON.parse(cachedQuestions));
        } else {
          const questionsResponse = await GetQuestions(id);
          setQuestions(questionsResponse.data);
          sessionStorage.setItem(
            `questions_${id}`,
            JSON.stringify(questionsResponse.data)
          );
        }
      } catch (error) {
        alert.show("Ошибка полученния данных квиза",{type:'error'});
      }
    }

    fetchQuizData();
  }, [id]);

  if (!quizData) {
    return <div>Загрузка...</div>;
  }
  return (
    <div
      className="quizmain_window"
      style={{ backgroundImage: "url(../img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="quizmain_workspace">
        <div className="quiz_card">
          <img
            src={
              quizData.image_name == null
                ? emptyQuizIcon
                : address + quizData.image_name
            }
            alt=""
            className="quiz_card__img"
          />
          <span className="quiz_card__footer">
            <div className="quiz_background"></div>
            <div className="quiz_quizname">{quizData.name}</div>
            <p className="quiz_information">{quizData.description}</p>
            <div className="quiz_control">
              <button className="btn" onClick={goToQuiz}>
                Пройти
              </button>
            </div>
          </span>
        </div>
      </div>
    </div>
  );
}

export default Quiz;
