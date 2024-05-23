import React, { useState, useEffect } from "react";
import Menu from "../components/Menu";
import { Find } from "../http/searchApi";
import "../css/font.css";
import { address } from "../http/apiIndex";
import "../css/main.css";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useAlert } from "react-alert";

import emptyQuizIcon from "../icons/emptyQuiz.svg";

function FindQuiz() {
  const alert = useAlert();
  const { page } = useParams();
  const { text } = useParams();
  const navigate = useNavigate();
  const [results, setResults] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    fetchData();
  }, [currentPage, text, page]);

  const fetchData = async () => {
    try {
      const res = await Find(parseInt(page), text);
      setResults(res.data.quizDataList);
    } catch (error) {
      alert.show(`Ошибка получения данных квизов`, { type: "error" });
    }
  };

  const handleNextPage = () => {
    const nextPage = parseInt(page) + 1;
    setCurrentPage(nextPage);
    navigate(`/${nextPage}/${text}`);
  };

  const handlePrevPage = () => {
    const prevPage = Math.max(parseInt(page) - 1, 0);
    setCurrentPage(prevPage);
    navigate(`/${prevPage}/${text}`);
  };

  const listItems = results.map((testname, index) => (
    <div className="card" key={index}>
      <img
        src={testname.image == null ? emptyQuizIcon : address + testname.image}
        alt=""
        className="card__img"
        style={{ marginLeft: "-5%" }}
      />
      <span className="card__footer">
        <div class="background"></div>
        <div className="quizname">{testname.name}</div>
        <p class="information">{testname.description}</p>
        <div class="control">
          <button class="btn" onClick={() => navigate(`/quiz/${testname.id}`)}>
            Пройти
          </button>
        </div>
      </span>
    </div>
  ));

  return (
    <div
      className="main_window"
      style={{ backgroundImage: "url(/img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="main_workspace">
        <div className="main_cardlist">
          <p className="h2" style={{ justifySelf: "center" }}>
            Найденные квизы{" "}
          </p>
          <h4 style={{ justifySelf: "center" }}>{`По запросу: ` + text} </h4>
          <h4 style={{ marginLeft: "54px" }}>
            {listItems.length !== 0
              ? `Страница ` + (parseInt(page) + 1)
              : `Здесь ничего нет`}
          </h4>
          {listItems}
          <div className="pagination">
            <button style={{ marginRight: "14px" }} onClick={handlePrevPage}>
              {"<"}
            </button>
            <div>{parseInt(page) + 1}</div>
            <button
              style={{ marginLeft: "14px" }}
              onClick={handleNextPage}
            >{`>`}</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FindQuiz;
