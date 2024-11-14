import { IconProps } from "./types";
import { Icon } from "@chakra-ui/react";

export function HardDrive(props: IconProps) {
    return (
        <Icon asChild {...props}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 32 33">
                <path
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M26.64 22.197v-.249c-.001-.378-.045-.755-.132-1.123l-2.474-10.517a3.682 3.682 0 0 0-3.583-2.838h-8.896a3.682 3.682 0 0 0-3.583 2.838L5.497 20.825c-.086.368-.13.745-.13 1.123v.249m21.272 0a3.273 3.273 0 0 1-3.273 3.273H8.64a3.273 3.273 0 0 1-3.273-3.273m21.273 0a3.272 3.272 0 0 0-3.273-3.273H8.64a3.273 3.273 0 0 0-3.273 3.273m18 0h.01v.009h-.01v-.01Zm-3.272 0h.008v.009h-.008v-.01Z"
                />
            </svg>
        </Icon>
    )
}
