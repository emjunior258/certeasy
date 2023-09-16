import { rest } from "msw";
import api, { SERVER } from "../config/config";

const mockIssuers = [
  {
    id: "276c1b219719fb06c308bc5b014d02d018866183",
    name: "myca5",
    type: "ROOT",
    serial: "1692646437803",
    dn: "CN=myca5, C=MZ, ST=Maputo, L=Boane, STREET=1234 Main Street",
    path_length: -1,
    children_count: 0,
  },
  {
    id: "dc5652bfee1796e64b410567b1406fd282602b39",
    name: "myca4",
    type: "ROOT",
    serial: "1692646434814",
    dn: "CN=myca4, C=MZ, ST=Maputo, L=Boane, STREET=1234 Main Street",
    path_length: -1,
    children_count: 0,
  },
  {
    id: "5d8db38052a5bf1a63f8bd8b9de3bc836d96cdcb",
    name: "myca8",
    type: "ROOT",
    serial: "1692646450065",
    dn: "CN=myca8, C=MZ, ST=Maputo, L=Boane, STREET=1234 Main Street",
    path_length: -1,
    children_count: 0,
  },
];

export const handlers = [
  // Handles a GET /issuers request
  rest.get(`${SERVER}/issuers`, (req, res, ctx) => {
    // Verify if the correct endpoint was used
    if (req.url.pathname === "/api/issuers") {
      // Respond with mock data
      return res(ctx.status(200), ctx.json(mockIssuers));
    } else {
      // Respond with an error if the endpoint doesn't match
      return res(ctx.status(404), ctx.json({ error: "Endpoint not found" }));
    }
  }),
];
