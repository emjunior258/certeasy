import { IconProps } from "./types";
import { Icon } from "@chakra-ui/react";

export function Download(props: IconProps) {
    return (
        <Icon asChild {...props}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 16 16">
                <path d="M15.031 11.75a.469.469 0 0 0-.468.469v2.048a.277.277 0 0 1-.235.296H1.672a.276.276 0 0 1-.234-.3v-2.044a.469.469 0 1 0-.938 0v2.048A1.207 1.207 0 0 0 1.672 15.5h12.656a1.207 1.207 0 0 0 1.172-1.238V12.22a.469.469 0 0 0-.469-.469Z"
                 fill="currentColor"
                 />
                <path d="M7.156 12.72a1.017 1.017 0 0 0 .844.408 1.016 1.016 0 0 0 .844-.408l4.476-4.626a.469.469 0 0 0-.673-.652L8.47 11.75V.969a.469.469 0 0 0-.938 0V11.75L3.354 7.442a.47.47 0 0 0-.675.652l4.477 4.626Z" 
                fill="currentColor"
                 />
            </svg>
        </Icon>
    )
}
