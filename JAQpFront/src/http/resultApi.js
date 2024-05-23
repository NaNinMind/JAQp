import { apiHost } from "./apiIndex";
const resultBase = "/api/result/";

export const MakeAnswer = async (_token, _id) =>
{
    return apiHost.get(resultBase+"answer/"+_id,  {
        headers: {
          Authorization: "Bearer " + _token,
        },
      });
}

export const MakeResult = async (_token, _id, _result) =>
{
    return apiHost.get(resultBase+"result",  {
       params : {
            id: _id,
            result: _result
        },
        headers: {
          Authorization: "Bearer " + _token,
        },
      }); 
}

export const GetUserResults = async (_id) =>
{
  return apiHost.get(resultBase+"user/"+_id)
}