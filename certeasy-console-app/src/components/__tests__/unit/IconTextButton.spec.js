import { mount } from "@vue/test-utils";
import { describe, it, expect } from "vitest";
import IconTextButton from "@/components/buttons/IconTextButton.vue";

describe("IconActionButton", () => {
  it("displays the correct text", () => {
    const props = {
      buttonProps: {
        text: "All",
      },
    };
    const wrapper = mount(IconTextButton, { props });
    const buttonText = wrapper.text();
    expect(buttonText).toContain("All");
  });
  it("displays the correct icon when passed", () => {
    const props = {
      buttonProps: {
        icon: "./src/assets/icons/sub-storage.svg",
        iconAlt: "sub-storage",
      },
    };
    const wrapper = mount(IconTextButton, { props });
    const icon = wrapper.find("img[alt='sub-storage']");

    expect(icon.exists()).toBe(true);
  });
  it("displays a primary colored button if active and not disabled", () => {
    const props = {
      buttonProps: { active: true },
    };
    const wrapper = mount(IconTextButton, { props });
    const button = wrapper.find("button");

    expect(button.classes("text-white")).toBe(true);
    expect(button.classes("bg-primary")).toBe(true);
  });
});
