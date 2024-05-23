import React, { useRef, useState, useEffect } from "react";
import { useAlert } from "react-alert";

import { address } from "../http/apiIndex";

import imageIcon from "../icons/image.svg";

import "../css/fileinput.css";


const FileInput = ({ callback, imageUrl }) => {
  const inputRef = useRef();
  const alert = useAlert();
  const handleCallback = () => callback(image, selectedFile, fileVariant);
  const [image, setImage] = useState(`${address}${imageUrl}`);
  const [selectedFile, setSelectedFile] = useState(
    image
      ? image.split("/").pop() === "null"
        ? null
        : image.split("/").pop()
      : null
  );

  const [showImage, setShowImage] = useState(
    image ? (image.split("/").pop() === "null" ? false : true) : false
  );
  const [fileVariant, setFileVariant] = useState(false);
  useEffect(() => {
    handleCallback();
  });

  /* func */
  const handleOnChange = (event) => {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      const allowedTypes = ["image/png", "image/jpeg", "image/gif", "image/jpg"];

      if (!allowedTypes.includes(file.type)) {
        alert.show("Разрешены только файлы PNG, JPEG, JPG и GIF",{type:"error"});
        return;
      }

      if (file.size > 25000000) {
        alert.show("Файл слишком большой",{type:"error"});
        return;
      }
      setFileVariant(true);
      setImage(URL.createObjectURL(file));
      setSelectedFile(file);
      setShowImage(true);
    }
  };

  const onChooseFile = () => {
    inputRef.current.click();
  };

  const removeFile = () => {
    setImage(null);
    setSelectedFile(null);
    setFileVariant(true);
    setShowImage(false);

    inputRef.current.value = "";

    if (image) {
      URL.revokeObjectURL(image);
    }
  };

  return (
    <div style={{ height: "80%" }}>
      <input
        type="file"
        ref={inputRef}
        onChange={handleOnChange}
        accept=".jpeg, .gif, .png, .jpg"
        style={{ display: "none" }}
      />
      <button
        className="file-btn"
        type="button"
        style={{
          backgroundImage:
            selectedFile != null ? `url(${image})` : selectedFile,
          backgroundSize: "cover",
          backgroundOrigin: "revert",
        }}
        onClick={onChooseFile}
      >
        {!showImage ? (
          <div className="inputBlock">
            <span
              className="material-symbols-rounded"
              style={{ marginLeft: "auto", marginRight: "auto" }}
            >
              Загрузить изображение
            </span>
            <p>JPEG, JPG, PNG или GIF (MAX. 25Мб)</p>
            <svg
              xmlnsXlink="http://www.w3.org/1999/xlink"
              style={{
                width: "68px",
                height: "68px",
              }}
            >
              <use xlinkHref={imageIcon + "#imageIcon"} />
            </svg>
          </div>
        ) : null}
      </button>
      {selectedFile && (
        <div className="selected-file">
          <p>{selectedFile.name || selectedFile}</p>
          <button onClick={removeFile}>
            <span className="material-symbols-rounded">х</span>
          </button>
        </div>
      )}
    </div>
  );
};

export default FileInput;
