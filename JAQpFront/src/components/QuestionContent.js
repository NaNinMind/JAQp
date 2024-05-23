import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useAlert } from "react-alert";

import { EditQuestionWOImage, GetQuestion } from "../http/questionApi";
import { EditQuestion } from "../http/questionApi";

import { AddAnswer } from "../http/answerApi";
import { GetAnswer } from "../http/answerApi";
import { DeleteAnswer } from "../http/answerApi";
import { ChangeAnswerWOImage } from "../http/answerApi";
import { ChangeAnswer } from "../http/answerApi";

import FileInput from "./FileInput";
import MessageAlert from "../components/alerts/MessageAlert";

import trashIcon from "../icons/trashCan.svg";
import questionIcon from "../icons/question.svg";

import "../css/tabcontent.css";

function TabContent({
  answers,
  label,
  description,
  image,
  id,
  onDeleteQuestion,
  onChangedQuestion,
}) {
  const [showAlert, setShowAlert] = useState(false);
  const alert = useAlert();
  /* visual */
  const handleShowAlert = () => {
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  /*------------------------question--------------------------- */
  /* setters */
  const [initAnswers, setAnswers] = useState([]);
  const [inittitle, setTitle] = useState(label);
  const [initdescription, setDescription] = useState(description);
  const [initimage, setImage] = useState(image ? image : null);
  const [initId, setInitId] = useState(id);

  useEffect(() => {
    setTitle(label);

    if (id !== undefined)
      GetQuestion(id).then((res) => {
        if (res.status === 200) {
          setAnswers(res.data.answers !== undefined ? res.data.answers : []);
          setDescription(res.data.description);
          setImage(res.data.image);
          setInitId(id);
        } else alert.show(`Ошибка получения данных вопроса`, { type: "error" });
      });
  }, [answers, description, label, image, id]);
  const { handleSubmit } = useForm();

  /* func */
  const handleDescriptionQuestionChange = (event) => {
    setDescription(event);
  };
  const handleIsDelete = (isDelete) => {
    handleShowAlert();
    if (isDelete === true) {
      alert.show(`Удаляю вопрос...`);
      onDeleteQuestion(id);
      handleCloseAlert();
    }
  };
  const onSubmit = () => {
    alert.show(`Сохраняю данные...`);
    const divElement = document.getElementById(`${id}/questionInput`);
    const fileInput = divElement.querySelector('input[type="file"]');
    const selectedFile = fileInput.files[0];
    const buttonElement = divElement.querySelector(".file-btn");
    const backgroundImageUrl = buttonElement.style.backgroundImage;

    !backgroundImageUrl.includes("blob") || backgroundImageUrl == null
      ? EditQuestionWOImage(
          localStorage.getItem("token"),
          initId,
          initdescription != null ? initdescription : ""
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные вопроса успешно сохранены`, {
                type: "success",
              });
              onChangedQuestion(res.data);
            } else alert.show(`Ошибка сохранения`, { type: "error" });
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения`, { type: "error" });
          })
      : EditQuestion(
          localStorage.getItem("token"),
          initId,
          initdescription != null ? initdescription : "",
          selectedFile,
          selectedFile ? selectedFile.name : null
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные вопроса успешно сохранены`, {
                type: "success",
              });
              onChangedQuestion(res.data);
            } else alert.show(`Ошибка сохранения`, { type: "error" });
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения`, { type: "error" });
          });
  };

  /*♿*/
  const callback = (image, file, fileVariant) => {};
  /*-------------------------answers-------------------------- */
  /* setters */
  const [answersData, setAnswersData] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      const updatedAnswersData = [];
      for (const answer of initAnswers) {
        try {
          var res = await GetAnswer(answer);
          updatedAnswersData.push(res.data);
        } catch (error) {
          alert.show(`Ошибка получения данных вопроса`, { type: "error" });
        }
      }
      setAnswersData(updatedAnswersData);
    };

    fetchData();
  }, [initAnswers]);

  /* func */
  const handleSaveAnswer = (answerId) => {
    alert.show(`Сохраняю данные...`);
    const divElement = document.getElementById(`${id}/answerInput/${answerId}`);
    const fileInput = divElement.querySelector('input[type="file"]');
    const selectedFile = fileInput.files[0];
    const buttonElement = divElement.querySelector(".file-btn");
    const backgroundImageUrl = buttonElement.style.backgroundImage;
    const textarea = document.getElementById(
      `${id}/answerDescription/${answerId}`
    ).value;
    const checkbox = document.getElementById(
      `${id}/answerCheck/${answerId}`
    ).checked;

    !backgroundImageUrl.includes("blob") || backgroundImageUrl == null
      ? ChangeAnswerWOImage(
          localStorage.getItem("token"),
          answerId,
          textarea != null ? textarea : "",
          checkbox
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные ответа успешно сохранены`, {
                type: "success",
              });
              onChangedQuestion(res.data);
            } else alert.show(`Ошибка сохранения`, { type: "error" });
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения`, { type: "error" });
          })
      : ChangeAnswer(
          localStorage.getItem("token"),
          answerId,
          textarea != null ? textarea : "",
          checkbox,
          selectedFile,
          selectedFile ? selectedFile.name : null
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные ответа успешно сохранены`, {
                type: "success",
              });
              onChangedQuestion(res.data);
            } else alert.show(`Ошибка сохранения`, { type: "error" });
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения`, { type: "error" });
          });
  };

  const handleDescriptionAnswerChange = (event, answerId) => {
    const textarea = document.getElementById(
      `${id}/answerDescription/${answerId}`
    );

    if (textarea) {
      textarea.value = event.target.value;
    }
  };

  const handleCheckboxChange = (event, answerId) => {
    document.getElementById(`${id}/answerLabel/${answerId}`).textContent = event
      .target.checked
      ? "Да"
      : "Нет";
  };

  const handleAddAnswer = () => {
    alert.show(`Добавляю ответ...`);
    AddAnswer(localStorage.getItem("token"), initId)
      .then((res) => {
        if (res.status === 200) {
          alert.show(`Ответ успешно добавлен`, {
            type: "success",
          });

          setAnswers([...initAnswers, res.data.id]);
        } else {
          alert.show(`Ошибка добавления ответа`, { type: "error" });
        }
      })
      .catch((error) => {
        alert.show(`Ошибка добавления ответа`, { type: "error" });
      })
      .finally(() => {
        setTimeout(() => {
          document.getElementById("content").scrollTo({
            top: document.getElementById("content").scrollHeight,
            behavior: "smooth",
          });
        }, 500);
      });
  };

  const handleDeleteAnswer = (id) => {
    alert.show(`Удаляю ответ...`);
    DeleteAnswer(localStorage.getItem("token"), id)
      .then((res) => {
        if (res.status === 200) {
          alert.show(`Ответ успешно удален`, {
            type: "success",
          });
          const newAnswers = initAnswers.filter((answerId) => answerId !== id);
          setAnswers(newAnswers);
        } else alert.show(`Ошибка удаления ответа`, { type: "error" });
      })
      .catch((error) => {
        alert.show(`Ошибка удаления ответа`, { type: "error" });
      });
  };

  return (
    <div className="content_tab_statebar">
      {showAlert && (
        <MessageAlert
          variant="danger"
          message="Вы действительно хотите удалить Вопрос? Действие нельзя отменить"
          title="Вы уверены?"
          onCancel={handleCloseAlert}
          onDelete={handleIsDelete}
        />
      )}
      <div style={{}}>
        <div
          className="content_tab_whitecardwithspace"
          style={{
            marginBottom: "36px",
            background: "white",
            padding: "14px",
            marginTop: "14px",
            display: "grid",
            gridTemplateColumns: "2fr 1fr",
            minWidth: "1154px",
          }}
        >
          <p className="h2" style={{ float: "right" }}>
            {inittitle}
          </p>
          <button
            type="button"
            className="main_buttondelstate"
            style={{ marginLeft: "60%" }}
            onClick={handleIsDelete}
          >
            <svg
              xmlnsXlink="http://www.w3.org/1999/xlink"
              style={{ width: "24px", height: "24px" }}
              className="trashIcon"
            >
              <use xlinkHref={trashIcon + "#trashCan"} />
            </svg>
            Удалить вопрос
          </button>
        </div>
        <form onSubmit={handleSubmit(onSubmit)} noValidate>
          <div className="content_tab_whitecardwithspace">
            <div
              id={`${id}/questionInput`}
              style={{ paddingTop: "14px", height: "100%", minHeight: "340px" }}
            >
              <FileInput
                key={initId}
                callback={callback}
                imageUrl={initimage}
              />
            </div>
            <div
              className="main_tab_evenly_distributed_field"
              style={{ marginTop: "54px", width: "100%" }}
            >
              <div class="omrs-input-group" style={{ marginTop: "-28px" }}>
                <label class="omrs-input-filled">
                  <textarea
                    required
                    maxLength={255}
                    value={initdescription ? initdescription : ""}
                    defaultValue={
                      initdescription != null ? initdescription : ""
                    }
                    onChange={(e) =>
                      handleDescriptionQuestionChange(e.target.value)
                    }
                    style={{ maxHeight: "260px" }}
                  />
                  <span class="omrs-input-label">Содержимое вопроса</span>
                </label>
              </div>
            </div>
            <button className="content_tab_button" type="submit">
              Сохранить вопрос
            </button>
          </div>
          <div className="content_tab_whitecardwithspace">
            <button
              onClick={handleAddAnswer}
              type="button"
              className="content_tab_button_add"
            >
              <span className="plus">+</span>
              Добавить ответ
            </button>
          </div>
        </form>
        <div className="content_tab_whitecardwithspace">
          <div className="content_answer_block">
            {answersData.map((answerData, index) => (
              <div key={answerData.id}>
                <button
                  onClick={() => handleSaveAnswer(answerData.id)}
                  type="button"
                  className="content_tab_button"
                  style={{ marginBottom: "24px" }}
                >
                  OK
                </button>
                <div
                  id={`${id}/answerInput/${answerData.id}`}
                  style={{
                    paddingTop: "14px",
                    height: "100%",
                    width: "100%",
                  }}
                >
                  <FileInput callback={callback} imageUrl={answerData.image} />
                </div>
                <div
                  class="omrs-input-group"
                  style={{ marginTop: "24px" }}
                  key={answerData.id}
                >
                  <label class="omrs-input-filled">
                    <textarea
                      id={`${id}/answerDescription/${answerData.id}`}
                      required
                      maxLength={255}
                      defaultValue={
                        answerData.content != null ? answerData.content : ""
                      }
                      onChange={(e) =>
                        handleDescriptionAnswerChange(e, answerData.id)
                      }
                      style={{
                        maxHeight: "128px",
                        width: "20px",
                        maxWidth: "20px",
                      }}
                    />
                    <span class="omrs-input-label">Содержимое ответа</span>
                  </label>
                </div>
                <fieldset>
                  <legend class="tooltip">
                    Ответ верный?
                    <svg
                      xmlnsXlink="http://www.w3.org/1999/xlink"
                      style={{
                        width: "18px",
                        height: "18px",
                        marginLeft: "14px",
                      }}
                    >
                      <use xlinkHref={questionIcon + "#questionIcon"} />
                    </svg>
                    <span class="tooltip-text">
                      Для создания опроса пометьте все ответы как верные
                    </span>
                  </legend>
                  <div>
                    <input
                      type="checkbox"
                      id={`${id}/answerCheck/${answerData.id}`}
                      onChange={(e) => handleCheckboxChange(e, answerData.id)}
                      defaultChecked={answerData.right}
                    ></input>
                    <label
                      for={`${id}/answerCheck/${answerData.id}`}
                      id={`${id}/answerLabel/${answerData.id}`}
                    >
                      {answerData.right ? "Да" : "Нет"}
                    </label>
                  </div>
                </fieldset>
                <button
                  onClick={() => handleDeleteAnswer(answerData.id)}
                  type="button"
                  className="main_buttondelstate"
                  style={{ height: "38px", width: "38px", marginTop: "-150px" }}
                >
                  <svg
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                    style={{ width: "24px", height: "24px" }}
                    className="trashIcon"
                  >
                    <use xlinkHref={trashIcon + "#trashCan"} />
                  </svg>
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default TabContent;
