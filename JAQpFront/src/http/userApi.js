import { apiHost } from "./apiIndex";
const authBase = "/api/auth/";
const userBase = "/api/users/";

export const LoginUser = async (_user, _pass) => {
  const raw = {
    username: _user,
    password: _pass,
  };
  try {
    return await apiHost.post(authBase + "authenticate", raw);
  } catch (error) {
    return null;
  }
};

export const RegisterUser = async (_username, _password) => {
  let result = true;
  var data = { username: _username, password: _password };
  result = await apiHost.post(authBase + "register", data);
  if (result.status === 200) {
    return true;
  } else {
    return false;
  }
};

export const Logout = async (_token) =>
{
  return apiHost.post(authBase+"logout", null, {
    headers: {
      Authorization: "Bearer " + _token,
    },
  })
}

export const GetUserGeneral = async (_id) => {
  var re = await apiHost.get(userBase + _id);
  return re;
};

export const SetUserGeneral = async (
  _token,
  _id,
  _firstName,
  _secondName,
  _lastName,
  _birdthDate
) => {
  const raw = {
    firstName: _firstName,
    lastName: _lastName,
    secondName: _secondName,
    birthDate: _birdthDate,
  };

  let config = {
    headers: {
      Authorization: "Bearer " + _token,
    },
  };

  return await apiHost.post(userBase + _id + "/setting/general", raw, {
    headers: {
      Authorization: "Bearer " + _token,
    },
  });
};
