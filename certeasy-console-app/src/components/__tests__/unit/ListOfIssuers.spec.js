import { mount, flushPromises } from "@vue/test-utils";
import {
  expect,
  vi,
  beforeAll,
  afterAll,
  afterEach,
  describe,
  it,
} from "vitest";

import { server } from "../../../mocks/server";
import ListOfIssuers from "../../../views/ListOfIssuers.vue";
import { rest } from "msw";
import { SERVER } from "../../../config/config";

// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: "error" }));
//  Close server after all tests
afterAll(() => server.close());
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers());

describe("ListOfIssuers", () => {
  it("loads list of issuers on component mount", async () => {
    const wrapper = mount(ListOfIssuers);
    await flushPromises();

    const issuers = wrapper.findAll("[data-test='issuer']");

    expect(issuers).toHaveLength(3);
    expect(issuers[0].text()).toContain("myca5");
    expect(issuers[1].text()).toContain("myca4");
    expect(issuers[2].text()).toContain("myca8");
  });

  it("displays loading component on loading state", async () => {
    const wrapper = mount(ListOfIssuers);
    expect(wrapper.find("[data-test='loading']").exists()).toBe(true);

    await flushPromises();

    expect(wrapper.find("[data-test='loading']").exists()).toBe(false);
  });

  it("displays issuerCardNoContent component if the list is empty", async () => {
    server.use(
      rest.get(`${SERVER}/issuers`, (req, res, ctx) => {
        return res(ctx.status(200), ctx.json([]));
      })
    );

    const wrapper = mount(ListOfIssuers);
    await flushPromises();
    expect(wrapper.find("[data-test='no-issuers']").exists()).toBe(true);
  });
});
