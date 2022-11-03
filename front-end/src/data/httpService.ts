import axios from "axios";
import Cookies from "universal-cookie";

export const getHttpService = () => {
  const cookies = new Cookies();
  const setAuthorization = (token: string) => {
    cookies.set("Authorization", token, { sameSite: "strict" });
  };

  return {
    axios: axios.create({
      headers: {'Authorization': cookies.get("Authorization")}
    }),
    setAuth: setAuthorization,
  };
};
