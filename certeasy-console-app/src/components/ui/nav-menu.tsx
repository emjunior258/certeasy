import { IconType } from "@/components/icons/types";
import { HStack, Text } from "@chakra-ui/react";
import React, { PropsWithChildren } from "react";
import { Link, LinkProps } from "react-router-dom";

interface NavMenuItemProps extends LinkProps {
    icon: IconType,
    label: string
}

function NavMenuItem({ icon: Icon, label, ...props }: NavMenuItemProps) {
    return (
        <HStack asChild gap="1" padding="1.5">
            <Link {...props}>
                <Icon
                    fontSize="2.25rem"
                    sm={{
                        fontSize: "1.625rem",
                    }}
                />
                <Text display="none" sm={{ display: 'block' }} textStyle="sm">{label}</Text>
            </Link>
        </HStack>
    )
}

export function NavMenu({ children }: PropsWithChildren) {
    return (
        <nav>
            <HStack asChild gap="0" sm={{ gap: "6" }}>
                <ul>
                    {React.Children.map(children, (child, index) => (
                        <li key={index}>
                            {child}
                        </li>
                    ))}
                </ul>
            </HStack>
        </nav>
    )
}

NavMenu.Item = NavMenuItem