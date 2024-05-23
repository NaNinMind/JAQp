import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useAlert } from "react-alert";

import { EditQuizWOImage } from "../http/quizApi";
import { EditQuiz } from "../http/quizApi";
import { ToggleVisabylity } from "../http/quizApi";

import FileInput from "./FileInput";
import Select from "react-select";
import MessageAlert from "../components/alerts/MessageAlert";

import trashIcon from "../icons/trashCan.svg";

import "../css/maintab.css";

function MainTab({
  quizData,
  options,
  onChangeTest,
  onChangeStatus,
  onDeleteQuiz,
}) {
  /* setters */
  const [state, setState] = useState(quizData.isPublic);
  const idQuiz = quizData.id;
  const [title, setTitle] = useState(quizData.name);
  const [description, setDescription] = useState(quizData.description);
  const [image, setImage] = useState(quizData.image_name);
  const [file, setFile] = useState();
  const [isChangedImage, setIsChangedImage] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const alert = useAlert();
  const selectedTagsFromOptions = quizData.tags
    ? quizData.tags.map((tag) => {
        const option = options.find((option) => option.value === tag);
        return option ? option : { value: tag, label: tag };
      })
    : null;
  const customStyles = {
    dropdownIndicator: (base) => ({
      ...base,
      cursor: "pointer",
    }),
    option: (base, { isDisabled }) => ({
      ...base,
      cursor: isDisabled ? "not-allowed" : "pointer",
    }),
    clearIndicator: (base) => ({
      ...base,
      cursor: "pointer",
    }),

    multiValueRemove: (base) => ({
      ...base,
      cursor: "pointer",
    }),
  };
  const [selectedTags, setSelectedTags] = useState(selectedTagsFromOptions);
  const callback = (image, file, fileVariant) => {
    setImage(image);
    setFile(file);
    setIsChangedImage(fileVariant);
  };
  /* visual */
  const handleShowAlert = () => {
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  /* func */
  const filterOption = (option, inputValue) => {
    return option.label.toLowerCase().startsWith(inputValue.toLowerCase());
  };

  const { handleSubmit } = useForm();
  const onSubmit = () => {
    alert.show(`Сохраняю данные...`);
    const tags = selectedTags
      ? `${selectedTags.map((item) => item.value).join(", ")}`
      : "";
    const sanitizedDescription = description ?? "";
    const sanitizedTitle = title ?? "";
    !isChangedImage
      ? EditQuizWOImage(
          localStorage.getItem("token"),
          idQuiz,
          tags,
          sanitizedDescription,
          sanitizedTitle
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные успешно сохранены`, { type: "success" });
              onChangeTest(res.data);
            }
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения данных`, { type: "error" });
          })
      : EditQuiz(
          localStorage.getItem("token"),
          idQuiz,
          tags,
          sanitizedDescription,
          sanitizedTitle,
          file,
          file ? file.name : null
        )
          .then((res) => {
            if (res.status === 200) {
              alert.show(`Данные успешно сохранены`, { type: "success" });
              onChangeTest(res.data);
            }
          })
          .catch((error) => {
            alert.show(`Ошибка сохранения данных`, { type: "error" });
          });
  };

  const handleStateChange = () => {
    alert.show(state ? `Скрываю квиз...` : `Публикую квиз`);
    ToggleVisabylity(localStorage.getItem("token"), idQuiz)
      .then((res) => {
        if (res.status === 200) {
          res.data.isPublic
            ? alert.show(`Квиз успешно опубликован`, { type: "success" })
            : alert.show(`Квиз успешно скрыт`, { type: "success" });

          onChangeStatus(res.data);
          setState(res.data.isPublic);
        } else alert.show(`Ошибка изменения статуса`, { type: "error" });
      })
      .catch((error) => {
        alert.show(`Ошибка изменения статуса`, { type: "error" });
      });
  };

  /*♿*/
  const handleIsDelete = (isDelete) => {
    handleShowAlert();
    if (isDelete === true) {
      onDeleteQuiz(idQuiz).then((status) => {
        if (status === "ok") {
          handleCloseAlert();
        }
      });
    }
  };

  const handleTagChange = (selectedOptions) => {
    setSelectedTags(selectedOptions);
  };

  return (
    <div className="main_tab_statebar">
      {showAlert && (
        <MessageAlert
          variant="danger"
          message="Вы действительно хотите удалить квиз? Действие нельзя отменить"
          title="Вы уверены?"
          onCancel={handleCloseAlert}
          onDelete={handleIsDelete}
        />
      )}
      <div
        style={{
          marginBottom: "36px",
          background: "white",
          padding: "14px",
          marginTop: "14px",
          display: "grid",
          gridTemplateColumns: "2fr 1fr",
        }}
      >
        <p className="h2" style={{ float: "right" }}>
          О квизе
        </p>
        <button
          onClick={handleIsDelete}
          type="button"
          className="main_buttondelstate"
          style={{ marginLeft: "60%" }}
        >
          <svg
            xmlnsXlink="http://www.w3.org/1999/xlink"
            style={{ width: "24px", height: "24px" }}
            className="trashIcon"
          >
            <use xlinkHref={trashIcon + "#trashCan"} />
          </svg>
          Удалить квиз
        </button>
      </div>
      <form onSubmit={handleSubmit(onSubmit)} noValidate>
        <div className="main_tab_whitecardwithspace">
          <div>
            <div className="settings_evenly_distributed_field">
              <div class="omrs-input-group">
                <label class="omrs-input-filled">
                  <input
                    required
                    defaultValue={title}
                    onChange={(e) => setTitle(e.target.value)}
                    maxLength={255}
                  />
                  <span class="omrs-input-label">Название</span>
                </label>
              </div>
            </div>

            <div
              className="main_tab_evenly_distributed_field"
              style={{ marginTop: "44px" }}
            >
              <div class="omrs-input-group" style={{height:"auto"}}>
                <label class="omrs-input-filled">
                  <textarea
                    required
                    defaultValue={description}
                    onChange={(e) => setDescription(e.target.value)}
                    maxLength={255}
                  />
                  <span class="omrs-input-label">Описание</span>
                </label>
              </div>
            </div>
            <div style={{display:"flex", flexDirection:"column",textAlign:"left" }}>
              <p
                className="bold_text"
                style={{ float: "left", paddingLeft: "14px" }}
              >
                Теги
              </p>
            </div>
            <div className="main_tab_evenly_distributed_field ">
              <Select
                className="main_tab_custominput_select"
                isMulti
                closeMenuOnSelect={false}
                options={options}
                value={selectedTags}
                placeholder="Пусто"
                onChange={handleTagChange}
                filterOption={filterOption}
                maxMenuHeight={186}
                theme={(theme) => ({
                  ...theme,
                  borderRadius: 0,
                  colors: {
                    ...theme.colors,
                    primary: "black",
                  },
                })}
                styles={customStyles}
                noOptionsMessage={() => "Пусто"}
              />
            </div>
          </div>

          <div style={{ paddingTop: "14px", height: "100%" }}>
            <FileInput callback={callback} imageUrl={image} />
          </div>
          <div
            className="main_tab_column_distributed_field"
            style={{ width: "192px" }}
          >
            <div style={{ float: "right" }}>
              <div>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginTop: "68px",
                  }}
                >
                  <div className="bold_text">Статус: </div>
                  <div className="bold_text">
                    {state ? "Опубликован" : "Скрыт"}
                  </div>
                </div>
                <button
                  className="main_tab_button"
                  onClick={handleStateChange}
                  type="button"
                >
                  {state ? "Скрыть" : "Опубликовать"}
                </button>
              </div>
            </div>
          </div>
        </div>

        <button className="main_tab_button" type="submit">
          Сохранить
        </button>
      </form>
    </div>
  );
}

export default MainTab;
