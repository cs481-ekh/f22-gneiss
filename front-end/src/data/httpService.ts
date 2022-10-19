import axios from 'axios';    
import Cookies from 'universal-cookie';

export const getHttpService = () => {
  const cookies = new Cookies();
  const setAuthorization = (token: string) => {
    cookies.set("Authorization", token, {sameSite: "strict"});
    axios.interceptors.request.use(
      function (config) {
        if (config.headers != null) {
          config.headers.Authorization = `${token}`;
        }
        return config;
      },
      function (error) {
        return Promise.reject(error);
      }
    );
  };


  if (cookies.get("Authorization") != null) {
    setAuthorization(cookies.get("Authorization"));
  }

  return {axios: axios, setAuth: setAuthorization}
}