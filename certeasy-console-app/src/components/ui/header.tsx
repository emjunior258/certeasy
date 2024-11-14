import { OpenAPI } from "@/components/icons/open-api";
import { Settings } from "@/components/icons/settings";
import { Logo } from "@/components/ui/logo";
import { NavMenu } from "@/components/ui/nav-menu";
import { Box, Container, HStack } from "@chakra-ui/react";

export function Header() {
    return (
        <Box bg="primary" w="full" color="white" py="4.5">
            <Container maxWidth="1250px">
                <HStack justify="space-between">
                    <Logo />
                    <NavMenu>
                        <NavMenu.Item to="https://google.com" icon={OpenAPI} label="Open API" />
                        <NavMenu.Item to="https://google.com" icon={Settings} label="Settings" />
                    </NavMenu>
                </HStack>
            </Container>
        </Box>
    )
}