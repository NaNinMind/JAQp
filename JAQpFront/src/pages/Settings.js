import DatePicker, { registerLocale, setDefaultLocale } from "react-datepicker";
import ru from "date-fns/locale/ru";
import { useForm } from "react-hook-form";
import React, { useState, useEffect } from "react";
import { useAlert } from 'react-alert'

import { GetUserGeneral } from "../http/userApi";
import { SetUserGeneral } from "../http/userApi";

import Menu from "../components/Menu";
import MessageAlert from "../components/alerts/MessageAlert";

import "react-datepicker/dist/react-datepicker.css";
import "react-datepicker/dist/react-datepicker-cssmodules.css";
import "../css/sett.css";
import "../css/font.css";

import trashIcon from "../icons/trashCan.svg";

function UserSettings() {
  /* setterts */
  const [startDate, setStartDate] = useState('');
  const preFetch = GetUserGeneral(localStorage.getItem("idUser"));
  const [preffirstName, setprefFirstName] = useState("");
  const [prefsecondName, setprefSecondName] = useState("");
  const [preflastName, setprefLastName] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const alert = useAlert();

  useEffect(() => {
    preFetch.then((res) => {
      if (res.status === 200) {
        setprefFirstName(res.data.firstName);
        setprefSecondName(res.data.secondName);
        setprefLastName(res.data.lastName);
        setStartDate(res.data.birthDate?new Date(res.data.birthDate):null);
      } else alert.show("Ошибка полученния данных пользователя",{type:'error'});
    });
  }, []);

  registerLocale("ru", ru);
  setDefaultLocale("ru");

  /* visual */
  const handleShowAlert = () => {
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  /* form */
  const { handleSubmit } = useForm();
  const onSubmit = () => {
    alert.show("Сохраняю...", {type:"info"});
    SetUserGeneral(
      localStorage.getItem("token"),
      localStorage.getItem("idUser"),
      document.getElementById("firstName").value,
      document.getElementById("secondName").value,
      document.getElementById("lastName").value,
      startDate?startDate.toISOString():null
    ).then((res) => {
      if (res.status === 200) {
        alert.show("Сохранено успешно", {type:"success"});
      } else alert.show(`Ошибка сохранения данных пользователя`, {type:"error"});
    });
  };

  return (
    <div
      className="settings_window"
      style={{ backgroundImage: "url(../img/background.svg)" }}
    >
      <div>
        <Menu />
      </div>
      <div className="settings_workspace">
        <div>
          {showAlert && (
            <MessageAlert
              variant="danger"
              message="Вы действительно хотите удалить аккаунт? Действие нельзя отменить"
              title="Вы уверены?"
              onCancel={handleCloseAlert}
            />
          )}
          <div
            className="settings_whitecard_with_space"
            style={{ marginBottom: "36px" }}
          >
            <p className="h1">Настройки</p>
          </div>
          <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <div className="settings_whitecard_with_space">
              <div className="settings_whitecard_with_space">
                <p className="h2">Личные данные</p>
              </div>
              <div className="settings_evenly_distributed_field">
                <div class="omrs-input-group">
                  <label class="omrs-input-filled">
                    <input
                      required
                      id="firstName"
                      defaultValue={preffirstName}
                      maxLength={255}
                    />
                    <span class="omrs-input-label">Имя</span>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="24"
                      height="24"
                      viewBox="0 0 24 24"
                    >
                      <path fill="none" d="M0 0h24v24H0V0z" />
                      <circle cx="15.5" cy="9.5" r="1.5" />
                      <circle cx="8.5" cy="9.5" r="1.5" />
                      <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm-5-6c.78 2.34 2.72 4 5 4s4.22-1.66 5-4H7z" />
                    </svg>
                  </label>
                </div>
              </div>
              <div className="settings_evenly_distributed_field">
                <div class="omrs-input-group">
                  <label class="omrs-input-filled">
                    <input
                      required
                      id="secondName"
                      defaultValue={prefsecondName}
                      maxLength={255}
                    />
                    <span class="omrs-input-label">Фамилия</span>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="24"
                      height="24"
                      viewBox="0 0 24 24"
                    >
                      <path fill="none" d="M0 0h24v24H0V0z" />
                      <circle cx="15.5" cy="9.5" r="1.5" />
                      <circle cx="8.5" cy="9.5" r="1.5" />
                      <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm-5-6c.78 2.34 2.72 4 5 4s4.22-1.66 5-4H7z" />
                    </svg>
                  </label>
                </div>
              </div>
              <div className="settings_evenly_distributed_field">
                <div class="omrs-input-group">
                  <label class="omrs-input-filled">
                    <input
                      required
                      id="lastName"
                      defaultValue={preflastName}
                      maxLength={255}
                    />
                    <span class="omrs-input-label">Отчество</span>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="24"
                      height="24"
                      viewBox="0 0 24 24"
                    >
                      <path fill="none" d="M0 0h24v24H0V0z" />
                      <circle cx="15.5" cy="9.5" r="1.5" />
                      <circle cx="8.5" cy="9.5" r="1.5" />
                      <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm-5-6c.78 2.34 2.72 4 5 4s4.22-1.66 5-4H7z" />
                    </svg>
                  </label>
                </div>
              </div>
              <div
                className="settings_evenly_distributed_field"
                style={{ marginLeft: "-56px" }}
              >
                <p
                  className="settings_help_text"
                  style={{
                    padding: "14px",
                    alignSelf: "center",
                    marginLeft: "68px",
                  }}
                >
                  День рождения
                </p>
                <div class="omrs-input-group">
                  <label class="omrs-input-filled">
                    <DatePicker
                      id="date"
                      selected={startDate}
                      onChange={(date) => setStartDate(date)}
                      dateFormat="dd.MM.yyyy"
                      locale="ru"
                      shouldCloseOnSelect
                      showYearDropdown
                      scrollableYearDropdown
                      yearDropdownItemNumber={101}
                      showMonthDropdown
                      maxDate={new Date()}
                      fixedHeight
                      showIcon
                    ></DatePicker>
                  </label>
                </div>
              </div>
              <button
                className="settings_button"
                style={{
                  marginTop: "48px",
                  marginLeft: "14px",
                  marginRight: "14px",
                }}
                type="submit"
              >
                Сохранить
              </button>
            </div>
          </form>
          <div
            className="settings_whitecard_with_space"
            style={{ marginTop: "36px" }}
          >
            <p className="h2">Сообщить об ошибке</p>
            <div className="settings_evenly_distributed_field">
              <button className="buttongit">GitHub</button>
              <button className="buttontg">Telegram</button>
              <button className="buttonvk">VK</button>
            </div>
          </div>
          <div
            className="settings_whitecard_with_space"
            style={{ marginTop: "36px", marginBottom: "14px" }}
          >
            <p className="h2">Удалить аккаунт</p>
            <div className="settings_evenly_distributed_field">
              <button className="settings_buttondel" onClick={handleShowAlert}>
                <svg
                  xmlnsXlink="http://www.w3.org/1999/xlink"
                  style={{ width: "24px", height: "24px" }}
                  className="trashIcon"
                >
                  <use xlinkHref={trashIcon + "#trashCan"} />
                </svg>
                Удалить
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserSettings;
