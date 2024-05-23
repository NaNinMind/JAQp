import { apiHost } from "./apiIndex";
const searchBase = "/api/search/find";

export const FindAll = async(_page) =>
{
    return apiHost.get(searchBase, {
        params : {
             page: _page
         }
        })
}

export const Find = async(_page, _text) =>
{
    return apiHost.get(searchBase, {
        params : {
             page: _page,
             text: _text
         }
        })
}