import { IconProps } from "./types";
import { Icon } from "@chakra-ui/react";

export function TrashCan(props: IconProps) {
    return (
        <Icon asChild {...props}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 17 17">
                <path d="M3 5v10a1 1 0 0 0 1 1h9a1 1 0 0 0 1-1V5M16 5a2 2 0 0 0-2-2H3a2 2 0 0 0-2 2M11 3V2a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1v1M5.375 6v8M8.375 6v8M11.375 6v8"
                    stroke="currentColor"
                    strokeWidth=".75"
                />
            </svg>
        </Icon>
    )
}
