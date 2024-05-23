import { apiHost } from "./apiIndex";

export const GetAllTags = async() =>
{
    return apiHost.get("/api/tag/all");
}