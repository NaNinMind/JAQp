import React, { useState } from "react";
import { Alert } from "react-bootstrap";

import "../../css/messagealert.css";

const MessageAlert = ({ variant, message, title, onCancel, onDelete }) => {
  const [show, setShow] = useState(true);

  const handleClose = () => setShow(false);

  const handleDelete = () => {
    onDelete(true);
  };

  const handleCancel = () => {
    handleClose();
    onCancel();
  };

  return (
    <>
      {show && (
        <div className="message_alert_overlay">
          <div className="message_alert_container">
            <Alert variant={variant}>
              <div className="message_alert_modal_dialog message_alert_modal_confirm">
                <div className="message_alert_modal_content">
                  <div className="message_alert_modal_header flex-column">
                    {/*здесь должен быть серьезный котик*/}
                    <h4 className="message_alert__modal_title w-100">
                      {title}
                    </h4>
                    <button
                      onClick={handleCancel}
                      type="button"
                      className="close"
                      data-dismiss="modal"
                      aria-hidden="true"
                    >
                      &times;
                    </button>
                  </div>
                  <div className="message_alert_modal_body">
                    <p>{message}</p>
                  </div>
                  <div className="message_alert_modal_footer justify-content-center">
                    <button
                      onClick={handleCancel}
                      type="button"
                      className="message_alert_btn message_alert_btn_secondary"
                      data-dismiss="modal"
                    >
                      Отмена
                    </button>
                    <button
                      type="button"
                      onClick={handleDelete}
                      className="message_alert_modal_confirm message_alert_btn_danger"
                    >
                      Удалить
                    </button>
                  </div>
                </div>
              </div>
            </Alert>
          </div>
        </div>
      )}
    </>
  );
};

export default MessageAlert;
