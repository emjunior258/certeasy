import { IconProps } from "./types";
import { Icon } from "@chakra-ui/react";

export function PlusSquare(props: IconProps) {
    return (
        <Icon asChild {...props}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 26 26">
                <rect opacity=".1" x=".5" y=".5" width="25" height="25" rx="4" fill="currentColor" />
                <path d="M18.227 13.323H7.774A.324.324 0 0 1 7.45 13c0-.178.146-.323.324-.323h10.453c.178 0 .323.145.323.323a.324.324 0 0 1-.323.323Z" stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round" />
                <path d="M13 18.55a.324.324 0 0 1-.323-.323V7.773c0-.177.145-.323.323-.323.178 0 .324.146.324.323v10.454a.324.324 0 0 1-.324.323Z" stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round" />
            </svg>
        </Icon>
    )
}
