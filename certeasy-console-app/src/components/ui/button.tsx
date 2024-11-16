import { IconType } from "@/components/icons/types";
import { ButtonProps as ChakraButtonProps, Button as ChakraButton, HStack } from "@chakra-ui/react";
import { Slot, Slottable } from "@radix-ui/react-slot";
import { forwardRef } from "react";


interface ButtonProps extends ChakraButtonProps {
    iconSize?: number
    iconLeft?: IconType
    iconRight?: IconType
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
    function Button({ children, asChild, iconLeft: IconLeft, iconRight: IconRight, ...props }, ref) {
        const Component = asChild ? Slot : "button";
        return (
            <HStack asChild>
                <ChakraButton {...props} asChild>
                    <Component ref={ref}>
                        {IconLeft && <IconLeft />}
                        <Slottable>
                            {children}
                        </Slottable>
                        {IconRight && <IconRight />}
                    </Component>
                </ChakraButton>
            </HStack>
        )
    }
)