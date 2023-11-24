import React from 'react'
import '../styles/Footer.css'
import github from '../imgs/social-media/github.png'
import facebook from '../imgs/social-media/facebook.png'
import instagram from '../imgs/social-media/instagram.png'
import linkedin from '../imgs/social-media/linkedin.png'

export default function Footer() {
        return (
                <footer>
                        <div class="footer-text">
                                <p>Created by Denys Tsebulia    © 2023</p>
                        </div>
                        <div class="social-media">
                                <a href="https://github.com/d3nnyyy/" target='_blank' rel='noreferrer noopener'>
                                        <img src={github} alt="github" />
                                </a>
                                <a href="https://www.facebook.com/profile.php?id=100008631392775" target='_blank' rel='noreferrer noopener'>
                                        <img src={facebook} alt="facebook" />
                                </a>
                                <a href="https://www.instagram.com/__d3n41k__/" target='_blank' rel='noreferrer noopener'>
                                        <img src={instagram} alt="instagram" />
                                </a>
                                <a href="https://linkedin.com/in/denys-tsebulia" target='_blank' rel='noreferrer noopener'>
                                        <img src={linkedin} alt="linkedin" />
                                </a>
                        </div>
                </footer>
        )
}
