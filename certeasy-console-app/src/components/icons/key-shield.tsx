import { IconProps } from "./types";
import { Icon } from "@chakra-ui/react";

export function KeyShield(props: IconProps) {
    return (
        <Icon asChild {...props}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 52 52">
                <rect x=".003" width="52" height="52" rx="4" fill="currentColor" fillOpacity=".08" />
                <rect x=".503" y=".5" width="51" height="51" rx="3.5" stroke="currentColor" strokeOpacity=".4" />
                <path d="M26.002 20.63a1.313 1.313 0 1 0 0-2.626 1.313 1.313 0 0 0 0 2.626Z" fill="url(#a)" />
                <path d="m37.045 14.671-10.033-4.444a2.433 2.433 0 0 0-2.053 0l-10 4.444a2.552 2.552 0 0 0-1.514 2.323v1.818a28.704 28.704 0 0 0 9.696 21.513l1.178 1.044c.943.841 2.39.841 3.333 0l1.178-1.044a28.69 28.69 0 0 0 9.73-21.547v-1.817c0-.977-.572-1.886-1.515-2.29Zm-9.292 10.47v8.821c0 .067-.034.135-.067.202l-1.448 1.549c-.1.134-.303.134-.438 0l-1.48-1.549c-.068-.067-.068-.135-.068-.202V32.75c0-.067.033-.168.1-.202l.876-.875a.307.307 0 0 0 0-.404l-.875-.875c-.068-.068-.101-.135-.101-.202v-.842c0-.068.033-.169.1-.202l.876-.875a.307.307 0 0 0 0-.404l-.875-.876c-.068-.067-.101-.134-.101-.202v-1.65c-1.684-.706-2.895-2.356-2.895-4.275a4.62 4.62 0 0 1 4.612-4.612c2.525 0 4.612 2.087 4.612 4.612.034 1.919-1.145 3.569-2.828 4.276Z" fill="url(#b)" />
                <defs>
                    <linearGradient id="a" x1="26.002" y1="10" x2="26.002" y2="48.316" gradientUnits="userSpaceOnUse">
                        <stop stopColor="currentColor" />
                        <stop offset="1" stopColor="currentColor" stopOpacity="0" />
                    </linearGradient>
                    <linearGradient id="b" x1="26.002" y1="10" x2="26.002" y2="48.316" gradientUnits="userSpaceOnUse">
                        <stop stopColor="currentColor" />
                        <stop offset="1" stopColor="currentColor" stopOpacity="0" />
                    </linearGradient>
                </defs>
            </svg>
        </Icon>
    )
}
