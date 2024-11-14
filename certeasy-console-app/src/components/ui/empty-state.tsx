import { IconType } from "@/components/icons/types";
import { Button, StackProps, Text, VStack } from "@chakra-ui/react";
import React from "react";

interface EmptyStateProps extends Omit<StackProps, 'as' | 'asChild' | 'children'> {
    icon?: IconType,
    title?: string
    description?: string
    action?: {
        icon: IconType,
        label: string
        onClick?: React.MouseEventHandler<HTMLButtonElement>
    }
}

export function EmptyState({ icon: Icon, title, description: subtitle, action, ...props }: EmptyStateProps) {
    return (
        <VStack w="full" {...props} sm={{ p: "10" }}>
            <VStack gap="6">
                {Icon && <Icon color="primary" fontSize="xxx-large" sm={{ fontSize: "9.875rem" }} />}
                {(title ?? subtitle) && (
                    <VStack gap="0.5">
                        {title && (
                            <Text
                                textAlign="center"
                                color="primary"
                                fontWeight="normal"
                                fontSize="large"
                                sm={{ fontSize: "1.75rem" }}
                            >
                                {title}
                            </Text>
                        )}
                        {subtitle && (
                            <Text
                                textAlign="center"
                                fontWeight="light"
                                fontSize="sm"
                                sm={{ fontSize: 'md' }}
                            >
                                {subtitle}
                            </Text>
                        )}
                    </VStack>
                )}
                {action && (
                    <Button
                        onClick={action.onClick}
                        py="3.5"
                        px="6"
                        variant="outline"
                        borderColor="primary"
                        color="primary"
                        fontWeight="semibold"
                        fontSize="large"
                        _hover={{
                            bgColor: 'primary',
                            color: 'white'
                        }}
                    >
                        {action.label}
                        <action.icon fontSize="1.5625rem" />
                    </Button>
                )}
            </VStack>
        </VStack>
    )
}