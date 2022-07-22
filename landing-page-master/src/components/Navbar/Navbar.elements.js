import styled from 'styled-components';
import {GoDeviceDesktop} from 'react-icons/go';
import { Container } from '../../globalStyles'; 

export const Nav = styled.nav`
background: #4C566A;
height: 80px;
display: flex;
justify-content: center;
align-items: center;
font-size: 1.5rem;
position: sticky;
top: 0;
z-index: 999;
`;

export const NavbarContainer = styled(Container)`
display: flex;
justify-content: center;
height: 80px;

${Container}
`
//logo text
export const NavLogo = styled.div`
color: #fff;
justify-self: flex-start;
cursor: pointer;
text-decoration: none;
font-size: 2rem;
display: flex;
align-items: center;
`
//logo icon
export const NavIcon = styled(GoDeviceDesktop)`
margin-right: 0.5rem;
`

