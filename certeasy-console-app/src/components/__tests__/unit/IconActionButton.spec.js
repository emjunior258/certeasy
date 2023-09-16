import { mount } from "@vue/test-utils";
import { describe, it, expect } from "vitest";
import IconActionButton from "@/components/buttons/IconActionButton.vue";

describe("IconActionButton", () => {
  it("displays the correct icon and text", () => {
    const props = {
      buttonProps: {
        icon: "./src/assets/icons/cog.svg",
        altIcon: "settings",
        text: "Settings",
        linkHref: "#",
      },
    };
    const wrapper = mount(IconActionButton, { props });
    const icon = wrapper.find("img[alt='settings']");
    const buttonText = wrapper.text();

    expect(icon.exists()).toBe(true);
    expect(buttonText).toContain("Settings");
  });
  it("displays an outlined button if prop outlined is passed", () => {
    const props = {
      buttonProps: {
        outlined: true,
      },
    };
    const wrapper = mount(IconActionButton, { props });
    const button = wrapper.find("button");

    expect(button.classes("border-primary")).toBe(true);
    expect(button.classes("bg-white")).toBe(true);
    expect(button.classes("text-primary")).toBe(true);
  });
  it("displays an solid button if prop outlined is not passed", () => {
    const props = {
      buttonProps: {},
    };
    const wrapper = mount(IconActionButton, { props });
    const button = wrapper.find("button");

    expect(button.classes("text-white")).toBe(true);
    expect(button.classes("bg-primary")).toBe(true);
  });
});
