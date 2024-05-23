import Media from "react-media";
import LongMenu from "./LongMenu";
import BurgerMenu from "./BurgerMenu";


function Menu() {
  return (
    <div>
      <Media query="(max-width: 767px)">
        {(matches) => (matches ? <BurgerMenu /> : <LongMenu />)}
      </Media>

    </div>
  );
}

export default Menu;
