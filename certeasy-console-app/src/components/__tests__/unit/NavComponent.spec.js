import { describe, it, expect, should } from "vitest";
import { mount } from "@vue/test-utils";
import NavComponent from "@/components/NavComponent.vue";

describe("Navbar", () => {
  const props = {
    logo: { imgSrc: "./src/assets/logo.svg", alt: "Certeasy" },
    navLinks: [
      {
        id: 0,
        icon: "./src/assets/icons/api.svg",
        altIcon: "API",
        text: "Open Api",
        linkHref: "#",
      },
      {
        id: 1,
        icon: "./src/assets/icons/cog.svg",
        altIcon: "settings",
        text: "Settings",
        linkHref: "#",
      },
    ],
  };
  it("Should render certeasy logo", () => {
    const wrapper = mount(NavComponent, { props: props });
    const logo = wrapper.find("img[alt='Certeasy']");
    expect(logo.exists()).toBe(true);
  });
  it("Should render a link to Open Api and another for settings", () => {
    const wrapper = mount(NavComponent, { props: props });
    const links = wrapper.findAll("a");
    expect(links.length).toBe(2);
    expect(links[0].text()).toBe("Open Api");
    expect(links[1].text()).toBe("Settings");
  });
});
