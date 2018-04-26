package com.iwith.voice.bean

class MachineResultBean {

    /**
     * status : true
     * responseCode : 10000
     * entry : [{"command":"speech","parameters":{"text":"这个问题太难了，请换一个吧","lang":"ZH"},"seq":1}]
     */

    var isStatus: Boolean = false
    var responseCode: String? = null
    var entry: List<EntryBean>? = null



    class EntryBean {

        /**
         * command : speech
         * parameters : {"text":"这个问题太难了，请换一个吧","lang":"ZH"}
         * seq : 1
         */

        var command: String? = null
        var parameters: ParametersBean? = null
        var seq: Int = 0

        class ParametersBean {
            /**
             * text : 这个问题太难了，请换一个吧
             * lang : ZH
             */

            var text: String? = null
            var lang: String? = null
            override fun toString(): String {
                return "ParametersBean(text=$text, lang=$lang)"
            }

        }

        override fun toString(): String {
            return "EntryBean(command=$command, parameters=$parameters, seq=$seq)"
        }
    }

    override fun toString(): String {
        return "MachineResultBean(isStatus=$isStatus, responseCode=$responseCode, entry=$entry)"
    }
}
