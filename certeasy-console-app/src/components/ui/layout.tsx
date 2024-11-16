import { Footer } from "@/components/ui/footer";
import { Header } from "@/components/ui/header";
import { Container, VStack } from "@chakra-ui/react";
import { Outlet } from "react-router";

export function Layout() {
    return (
        <VStack justify="space-between" minH="dvh" gap={"0"}>
            <Header />
            <Container maxWidth="1250px" flex={1}>
                <Outlet />
            </Container>
            <Footer mt="6" />
        </VStack>
    )
}