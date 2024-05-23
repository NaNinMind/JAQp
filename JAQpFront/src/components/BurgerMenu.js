import { useNavigate } from "react-router-dom";
import React, { useState } from "react";

import menuIcon from "../icons/menu.svg";
import searchIcon from "../icons/search.svg";
import crossIcon from "../icons/cross.svg";

import "../css/burgermenu.css";

function BurgerMenu() {
  /* navigate */
  const navigate = useNavigate();
  const gotoUserPage = () =>
    navigate(`/user/${localStorage.getItem("idUser")}`);
  const gotoConstructorPage = () => navigate("/constructor");
  const gotoLoginPage = () => navigate("/login");
  const gotoMainPage = () => navigate("/");

  /* setterts */
  const [show, setShow] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);

  const openSearch = () => {
    setIsSearchOpen(true);
  };

  const closeSearch = () => {
    setIsSearchOpen(false);
  };
  const [searchText, setSearchText] = useState("");
  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      navigate(`/0/${searchText}`);
    }
  };

  const searchOverlay = (
    <div className={`search_overlay ${isSearchOpen ? "open" : ""}`}>
      <div className="search_container">
        <input
          type="text"
          placeholder="Введите запрос"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <button onClick={closeSearch}>x</button>
      </div>
    </div>
  );

  /* visual */
  const showMenu = () => {
    setShow(!show);
  };

  return (
    <>
      <div className="burger_menu_container" style={{ background: "white" }}>
        <div className="burger_menu_block">
          <div className="burger_menu_evenly_distributed_block">
            <img
              className="burger_menu_button_icon_small"
              src={searchIcon}
              onClick={openSearch}
              alt=""
            ></img>
          </div>
          <div className="burger_menu_evenly_distributed_block">
            <p className="burger_menu_logo_name">JAQp</p>
          </div>
          <div
            className="burger_menu_evenly_distributed_block"
            onClick={showMenu}
          >
            {show ? (
              <img
                className="burger_menu_button_icon_small"
                src={crossIcon}
                alt=""
              ></img>
            ) : (
              <img
                className="burger_menu_button_icon_small"
                src={menuIcon}
                alt=""
              ></img>
            )}
          </div>
        </div>

        <div className={`burger_menu_items ${show ? "is_open" : ""}`}>
          <ul>
            <li>
              <div>
                <li>Случайный</li>
              </div>
            </li>
            <li onClick={gotoMainPage}>
              <div>
                <li>Квизы</li>
              </div>
            </li>
            <li>
              <div>
                <li>Лучшее</li>
              </div>
            </li>
            <li>
              <div
                onClick={
                  localStorage.getItem("token") === null ||
                  localStorage.getItem("token") === undefined
                    ? gotoLoginPage
                    : gotoConstructorPage
                }
              >
                <li>Конструктор</li>
              </div>
            </li>
            <li>
              <div
                onClick={
                  localStorage.getItem("token") === null ||
                  localStorage.getItem("token") === undefined
                    ? gotoLoginPage
                    : gotoUserPage
                }
              >
                <li>Профиль</li>
              </div>
            </li>
          </ul>
        </div>
      </div>
      {isSearchOpen && searchOverlay}
    </>
  );
}

export default BurgerMenu;
